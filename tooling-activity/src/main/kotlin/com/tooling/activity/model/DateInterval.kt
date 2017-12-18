package com.tooling.activity.model

import java.time.Instant

open class DateInterval(val userIds: List<String>? = null,
                        val projects: List<String>? = null,
                        var startDate: Instant? = null,
                        var endDate: Instant? = null,
                        var grpByType: Boolean = false,
                        var grpByUser: Boolean = true,
                        var grpByCode: Boolean = true)