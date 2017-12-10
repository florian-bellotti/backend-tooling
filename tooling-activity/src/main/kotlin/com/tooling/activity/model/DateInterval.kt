package com.tooling.activity.model

import java.time.Instant

open class DateInterval(val userIds: List<String>,
                        var startDate: Instant,
                        var endDate: Instant)