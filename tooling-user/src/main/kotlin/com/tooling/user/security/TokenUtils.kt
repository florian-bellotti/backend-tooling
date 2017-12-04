package com.tooling.user.security

import com.tooling.core.exception.InvalidTenantIdException
import com.tooling.user.exception.InvalidTokenException
import com.tooling.user.model.User
import com.tooling.user.service.AuthenticationService
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.time.Instant
import java.util.*

@Component
class TokenUtils(private val jwtParser: JwtParser,
                 private val jwtBuilder: JwtBuilder) {

  @Suppress("UNCHECKED_CAST")
  fun buildUserFromToken(authToken: String): User {
    try {
      val claims = jwtParser.parseClaimsJws(authToken).body
      val grpClaims = claims["grp"]
      val groups = if (grpClaims == null) mutableListOf() else grpClaims as MutableList<String>
      val tenantId = claims[AuthenticationService.TENANT_ID] as String?
      if (!StringUtils.hasText(tenantId))
        throw InvalidTenantIdException("Tenant id is null or empty in JWT.")

      val userId = claims["usr"] as String?
      if (!StringUtils.hasText(userId))
        throw InvalidTokenException("User id in token is null.")

      return User(id = userId, email = claims.subject, password = "", groups = groups, tenantId = tenantId!!)
    } catch (e: JwtException) {
      throw InvalidTokenException(e.message)
    }
  }

  fun buildTokenFromUser(user: User, expirationDate: Instant, tenantId: String): JwtBuilder {
    var jwt = this.jwtBuilder
      .setSubject(user.email)
      .setId(user.email)
      .setExpiration(Date.from(expirationDate))
      .setIssuedAt(Date.from(Instant.now()))
      .claim("grp", Optional.ofNullable<List<String>>(user.groups).orElse(emptyList()))
      .claim("loc", user.locale.toString())
      .claim(AuthenticationService.TENANT_ID, tenantId)

    jwt = jwt.claim("usr", user.id)
    jwt = jwt.claim("mail", user.email)
    if (user.firstName != null)
      jwt = jwt.claim("fnm", user.firstName)
    if (user.lastName != null)
      jwt = jwt.claim("lnm", user.lastName)

    return jwt;
  }
}