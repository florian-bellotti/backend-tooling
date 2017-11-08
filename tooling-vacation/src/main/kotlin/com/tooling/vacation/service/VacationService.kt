package com.tooling.vacation.service

import com.tooling.vacation.exception.InvalidParametersException
import com.tooling.vacation.model.Vacation
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class VacationService {

  fun check(holidayMono: Mono<Vacation>): Mono<Vacation> {
    return holidayMono
      .flatMap { holiday ->
        if (holiday.endDate == null || holiday.startDate == null) {
          Mono.error(InvalidParametersException("Start date or end date are empty"))
        } else if (holiday.endDate!!.before(holiday.startDate)) {
          Mono.error(InvalidParametersException("End date is before start date"))
        } else {
          Mono.just(holiday)
        }
      }
  }
}