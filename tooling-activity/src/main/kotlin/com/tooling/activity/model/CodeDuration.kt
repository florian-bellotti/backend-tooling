package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CodeDuration(var code: String? = null,
                        var userId: String? = null,
                        var duration: Long,
                        var typeCode: String? = null)