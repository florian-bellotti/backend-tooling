package com.tooling.user.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(var id: String? = null,
                   var email: String,
                   var firstName: String? = null,
                   var lastName: String? = null,
                   var groups: MutableList<String> = mutableListOf(Group.USER),
                   var address: String? = null,
                   var phone: String? = null,
                   var active: Boolean,
                   var locale: Locale) {
  constructor(user: User): this(user.id!!, user.email, user.firstName, user.lastName, user.groups, user.address, user.phone, user.active, user.locale)
}