package com.tooling.user.service

import com.tooling.core.exception.InvalidTenantIdException
import com.tooling.user.config.SecurityProperties
import com.tooling.user.exception.DisabledUserException
import com.tooling.user.exception.InvalidCredentialsException
import com.tooling.user.model.Auth
import com.tooling.user.model.User
import com.tooling.user.model.UserLogin
import com.tooling.user.respository.UserRepository
import io.jsonwebtoken.JwtBuilder
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCrypt.checkpw
import org.springframework.stereotype.Service
import java.time.Instant.now
import java.util.Date.from
import java.util.Optional.ofNullable

@Service
open class AuthenticationService(private val userRepository: UserRepository,
                                 private val jwtBuilder: JwtBuilder,
                                 private val securityProperties: SecurityProperties) {
  companion object {
    val logger = LoggerFactory.getLogger(AuthenticationService::class.java)!!
    val TENANT_ID = "tenant"
  }

  open fun authenticate(userLogin: UserLogin): Auth {
    val email = userLogin.email.toLowerCase()
    val tenantId = userLogin.tenantId

    if (tenantId.isEmpty())
      throw InvalidTenantIdException("Tenant id empty in userLogin")

    val user = ofNullable(userRepository.findByEmailAndTenantId(email, tenantId))
      .orElseThrow({ InvalidCredentialsException("Invalid login") })

    if (!user.active)
      throw DisabledUserException("User ${userLogin.email} is disabled")

    if (!checkPassword(user, userLogin.password)) {
      logger.info("Invalid login {} ", userLogin.email)
      throw InvalidCredentialsException("Invalid login")
    }

    val expirationDate = now().plus(securityProperties.tokenExpiration)

    var jwtBuilder = this.jwtBuilder
      .setSubject(userLogin.email)
      .setId(userLogin.email)
      .setExpiration(from(expirationDate))
      .setIssuedAt(from(now()))
      .claim("grp", ofNullable<List<String>>(user.groups).orElse(emptyList()))
      .claim("loc", user.locale.toString())
      .claim(TENANT_ID, tenantId)

    jwtBuilder = jwtBuilder.claim("usr", user.id)
    jwtBuilder = jwtBuilder.claim("mail", user.email)
    if (user.firstName != null)
      jwtBuilder = jwtBuilder.claim("fnm", user.firstName)
    if (user.lastName != null)
      jwtBuilder = jwtBuilder.claim("lnm", user.lastName)

    return Auth(user, jwtBuilder.compact())
  }

  private fun checkPassword(user: User, password: String): Boolean {
    var valid = false
    try {
      valid = checkpw(password, user.password)
    } catch (ignored: IllegalArgumentException) {
    }
    return valid
  }
}