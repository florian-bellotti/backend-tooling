package com.tooling.vacation.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Vacation(@Id
                    var id: String?,
                    var companyId: String?,
                    var userId: String?,
                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                    var startDate: Date?,
                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                    var endDate: Date?,
                    var status: VacationStatus?)