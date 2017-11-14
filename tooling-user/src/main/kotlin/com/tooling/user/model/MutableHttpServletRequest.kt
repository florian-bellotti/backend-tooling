package com.tooling.user.model

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class MutableHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

  private val customHeaders: MutableMap<String, String>

  init {
    this.customHeaders = HashMap()
  }

  fun putHeader(name: String, value: String) {
    this.customHeaders.put(name, value)
  }

  override fun getHeader(name: String): String? {
    // check the custom headers first
    val headerValue = customHeaders[name]
    if (headerValue != null){
      return headerValue;
    }

    // else return from into the original wrapped object
    return (request as HttpServletRequest).getHeader(name)
  }

  override fun getHeaderNames(): Enumeration<String> {
    val set = HashSet(customHeaders.keys)
    val e = (getRequest() as HttpServletRequest).getHeaderNames()
    while (e.hasMoreElements()) {
      val n = e.nextElement()
      set.add(n)
    }
    return Collections.enumeration(set)
  }
}
