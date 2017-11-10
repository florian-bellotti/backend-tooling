package com.tooling.user.model

import java.util.*

data class Auth(val email: String,
           val token: String,
           val firstName: String?,
           val lastName: String?,
           val groups: List<String>,
           val locale: Locale)
{
  constructor(user: User, token: String): this(user.email, token, user.firstName, user.lastName, user.groups, user.locale)
}
