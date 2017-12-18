package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@CompoundIndexes(
  CompoundIndex(name = "activity_projectId_tenantId_idx", def = "{ '_id': 1, 'tenantId': 1 }", background = true, unique = true)
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Activity(@Id
                    var id: String? = null,
                    var userId: String,
                    var code: String,
                    var typeCode: String,
                    var duration: Long,
                    var startDate: Instant,
                    var endDate: Instant,
                    var comment: String? = null,
                    var tenantId: String)