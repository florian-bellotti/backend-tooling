package com.tooling.project.model

import org.springframework.http.HttpStatus

class ErrorResponse(val code: HttpStatus,
                    val message: String? = "Unkown error")