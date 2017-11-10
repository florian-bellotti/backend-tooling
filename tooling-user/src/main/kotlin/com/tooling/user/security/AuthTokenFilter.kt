package com.tooling.user.security

import com.tooling.core.http.RemoteIPResolver
import com.tooling.instance.exception.InvalidInstanceIdException
import com.tooling.user.exception.InvalidTokenException
import com.tooling.user.model.User
import com.tooling.user.service.AuthenticationService
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@ConditionalOnClass(RestController::class)
class AuthTokenFilter(private val jwtParser: JwtParser) : GenericFilterBean() {
  companion object {
    val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)!!
    val TOKEN_HEADER_NAME = "Authorization"
  }

  override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
    val httpServletRequest = servletRequest as HttpServletRequest
    val authToken = httpServletRequest.getHeader(TOKEN_HEADER_NAME)?.replace("Bearer ", "") ?: ""

    try {
      if (StringUtils.hasText(authToken)) {
        val userInstance = buildUserFromToken(authToken)
        SecurityContextHolder.getContext().authentication = KeybuildAuthentication(userInstance.user)
      } else {
        SecurityContextHolder.getContext().authentication = null
      }

      filterChain.doFilter(servletRequest, servletResponse)
    } catch (ex: InvalidTokenException) {
      Companion.logger.debug("{} token invalid from {}: {}", TOKEN_HEADER_NAME,
        RemoteIPResolver.resolveRemoteIP(httpServletRequest), ex.message)
      SecurityContextHolder.getContext().authentication = null
      val response = servletResponse as HttpServletResponse
      response.status = HttpStatus.FORBIDDEN.value()
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun buildUserFromToken(authToken: String): UserInstance {
    try {
      val claims = jwtParser.parseClaimsJws(authToken).body
      val grpClaims = claims["grp"]
      val groups = if (grpClaims == null) mutableListOf() else grpClaims as MutableList<String>
      val instanceId = claims[AuthenticationService.INSTANCE_ID] as String?
      if (!StringUtils.hasText(instanceId)) {
        throw InvalidInstanceIdException("Instance id is null or empty in JWT.")
      }
      val user = User(email = claims.subject, password = "", groups = groups, instanceId = instanceId!!)

      return UserInstance(user, instanceId)
    } catch (e: JwtException) {
      throw InvalidTokenException(e.message)
    }
  }
}

internal data class UserInstance(val user: User,
                                 val instanceId: String)
