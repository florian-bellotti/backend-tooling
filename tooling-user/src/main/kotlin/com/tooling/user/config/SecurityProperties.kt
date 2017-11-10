package com.tooling.user.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "tooling.security")
open class SecurityProperties {
  companion object {
    val logger = LoggerFactory.getLogger(SecurityProperties::class.java)
  }

  var rsaPrivateKey: String? = null
  var rsaPublicKey: String? = null
  var tokenExpiration: Duration = Duration.parse("PT4H")

  @PostConstruct
  fun showInfo() {
    //logger.info("RSA Public Key: {}", Base64.getEncoder().encodeToString(rsaPublicKey))
    logger.info("RSA Public Key: {}", rsaPublicKey)
  }
}