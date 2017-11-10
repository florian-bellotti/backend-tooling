package com.tooling.user.security

import com.tooling.user.model.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class KeybuildAuthentication(private val user: User): Authentication {
  private val authorities = user.groups.map { SimpleGrantedAuthority(it) }

  override fun getAuthorities() = authorities

  override fun setAuthenticated(authenticated: Boolean) {
    if (!authenticated)
      throw IllegalArgumentException()
  }

  override fun getName() = user.email

  override fun getCredentials() = null

  override fun getPrincipal() = user

  override fun isAuthenticated() = true

  override fun getDetails() = null
}
