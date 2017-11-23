package com.tooling.core.http

import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest

class RemoteIPResolver {

  companion object {
    private val logger = LoggerFactory.getLogger(RemoteIPResolver::class.java)
    private val UNKNOWN_IP = "unknown"

    fun resolveRemoteIP(request: HttpServletRequest): String {
      if (logger.isTraceEnabled) {
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
          val key = headerNames.nextElement()
          val value = request.getHeader(key)
          logger.trace("Element http Header :{} -> {}", key, value)
        }
      }

      var ip: String? = request.getHeader("X-Forwarded-For")
      if (ip == null || ip.length == 0 || UNKNOWN_IP.equals(ip, ignoreCase = true)) {
        ip = request.getHeader("Proxy-Client-IP")
      }
      if (ip == null || ip.length == 0 || UNKNOWN_IP.equals(ip, ignoreCase = true)) {
        ip = request.getHeader("WL-Proxy-Client-IP")
      }
      if (ip == null || ip.length == 0 || UNKNOWN_IP.equals(ip, ignoreCase = true)) {
        ip = request.getHeader("HTTP_CLIENT_IP")
      }
      if (ip == null || ip.length == 0 || UNKNOWN_IP.equals(ip, ignoreCase = true)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR")
      }
      if (ip == null || ip.length == 0 || UNKNOWN_IP.equals(ip, ignoreCase = true)) {
        ip = request.remoteAddr
      }
      return ip!!
    }
  }
}