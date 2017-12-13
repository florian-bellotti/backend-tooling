package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ActivityTypeDto(val id: String? = null,
                           var code: String,
                           var name: String) {
  constructor(activityType: ActivityType):
    this(activityType.id, activityType.code, activityType.name)
}