package com.tooling.tenant.model

import org.springframework.data.annotation.Id

data class Tenant(@Id
                  val id: String? = null,
                  val name: String)
