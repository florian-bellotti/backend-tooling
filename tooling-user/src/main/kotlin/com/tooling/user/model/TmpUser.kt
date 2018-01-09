package com.tooling.user.model

import java.time.Instant
import java.util.*

data class TmpUser(var email: String,
                   var password: String,
                   var firstName: String? = null,
                   var lastName: String? = null,
                   var groups: MutableList<String> = mutableListOf(Group.USER),
                   var address: String? = null,
                   var phone: String? = null,
                   var workDuration: Number? = null,
                   var active: Boolean = true,
                   var locale: Locale = Locale.getDefault(),
                   var activationDate: Instant = Instant.now(),
                   var creationDate: Instant = Instant.now())