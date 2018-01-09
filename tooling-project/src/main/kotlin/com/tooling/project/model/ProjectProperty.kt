package com.tooling.project.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProjectProperty(var duration: Number,
                           var startDate: Instant? = null,
                           var endDate: Instant? = null)