package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndexes(
  CompoundIndex(name = "activityType_code_tenantId_idx", def = "{ 'code': 1, 'tenantId': 1 }", background = true, unique = true)
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ActivityType(@Id
                        val id: String? = null,
                        var code: String,
                        var name: String,
                        var tenantId: String)