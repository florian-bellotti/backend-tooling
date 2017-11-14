package com.tooling.zuul.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.LoggerFactory

class SimpleFilter: ZuulFilter() {

  companion object {
    val logger = LoggerFactory.getLogger(SimpleFilter::class.java)
    val TENANT_ID = "tenantId"
  }

  override fun run(): Any? {
    val ctx = RequestContext.getCurrentContext()
    val request = ctx.getRequest()
    ctx.addZuulRequestHeader(TENANT_ID, request.getHeader(TENANT_ID))
    logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()))
    return null
  }

  override fun shouldFilter() = true

  override fun filterType() = "pre"

  override fun filterOrder() = 1
}