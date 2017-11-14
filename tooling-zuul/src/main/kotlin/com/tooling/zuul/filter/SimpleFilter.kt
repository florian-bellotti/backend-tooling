package com.tooling.zuul.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.LoggerFactory

class SimpleFilter: ZuulFilter() {

  companion object {
    val logger = LoggerFactory.getLogger(SimpleFilter::class.java)
  }

  override fun run(): Any? {
    val ctx = RequestContext.getCurrentContext()
    val request = ctx.getRequest()
    ctx.addZuulRequestHeader("tenantId", "123456")
    logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()))
    return null
  }

  override fun shouldFilter() = true

  override fun filterType() = "pre"

  override fun filterOrder() = 1
}