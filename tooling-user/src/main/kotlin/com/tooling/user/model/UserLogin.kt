package com.tooling.user.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserLogin(var email: String,
                     var password: String,
                     var tenantId: String)
