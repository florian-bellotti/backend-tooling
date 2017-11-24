package com.tooling.user.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.tooling.user.model.Group.USER
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.Instant.now
import java.util.*

@Document
@CompoundIndexes(
  CompoundIndex(name = "user_email_tenantId_idx", def = "{ 'email': 1, 'tenantId': 1 }", background = true, unique = true)
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(@Id
                var id: String? = null,
                var email: String,
                var password: String,
                var firstName: String? = null,
                var lastName: String? = null,
                var groups: MutableList<String> = mutableListOf(USER),
                var address: String? = null,
                var phone: String? = null,
                var active: Boolean = true,
                var locale: Locale = Locale.getDefault(),
                var activationDate: Instant = now(),
                var creationDate: Instant = now(),
                val tenantId: String)

