package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ActivityDto(var id: String? = null,
                       var userId: String,
                       var code: String,
                       var typeCode: String,
                       var duration: Long? = null,
                       var startDate: Instant,
                       var endDate: Instant,
                       var comment: String? = null) {

  constructor(activity: Activity):
    this(activity.id, activity.userId, activity.code, activity.typeCode, activity.duration,
      activity.startDate, activity.endDate, activity.comment)
}