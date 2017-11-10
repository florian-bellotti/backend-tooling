package com.tooling.user.config

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.SignatureAlgorithm.RS256
import io.jsonwebtoken.impl.DefaultJwtBuilder
import io.jsonwebtoken.impl.DefaultJwtParser
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
@EnableConfigurationProperties(SecurityProperties::class)
open class SecurityConfiguration {
  @Bean
  open fun jwtBuilder(securityProperties: SecurityProperties): JwtBuilder {
    val keyFactory = KeyFactory.getInstance("RSA")
    val ks = PKCS8EncodedKeySpec(Base64.getDecoder().decode(securityProperties.rsaPrivateKey))
    val privateKey = keyFactory.generatePrivate(ks) as RSAPrivateKey
    return DefaultJwtBuilder().signWith(RS256, privateKey)
  }

  @Bean
  open fun jwtParser(securityProperties: SecurityProperties): JwtParser {
    val parser = DefaultJwtParser()
    val spec = X509EncodedKeySpec(Base64.getDecoder().decode(securityProperties.rsaPublicKey))
    val kf = KeyFactory.getInstance("RSA")
    parser.setSigningKey(kf.generatePublic(spec))
    return parser
  }
}