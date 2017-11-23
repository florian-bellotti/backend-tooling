package com.tooling.activity.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ActivityDto(var id: String? = null,
                       var userId: String,
                       var code: String,
                       var startDate: Instant,
                       var endDate: Instant,
                       var comment: String? = null) {

  constructor(activity: Activity):
    this(activity.id, activity.userId, activity.code, activity.startDate, activity.endDate, activity.comment)
}