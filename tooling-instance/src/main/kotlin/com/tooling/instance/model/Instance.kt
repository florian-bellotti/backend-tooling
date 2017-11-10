package com.tooling.instance.model

import org.springframework.data.annotation.Id

data class Instance(@Id
                    val id: String? = null,
                    val name: String)
