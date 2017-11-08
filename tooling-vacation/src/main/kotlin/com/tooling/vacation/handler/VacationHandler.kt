package com.tooling.vacation.handler

import com.tooling.vacation.model.Vacation
import com.tooling.vacation.repository.VacationRepository
import com.tooling.vacation.service.VacationService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class VacationHandler(private val vacationRepository: VacationRepository,
                      private val vacationService: VacationService) {

  fun find(): Mono<ServerResponse> =
    ServerResponse.ok().body(vacationRepository.findAll(), Vacation::class.java)

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(Vacation::class.java)
      .transform(vacationService::check)
      .flatMap(vacationRepository::insert)
      .transform { v -> ServerResponse.ok().body(v, Vacation::class.java) }


  fun update(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(Vacation::class.java)
      .transform(vacationService::check)
      .flatMap { holiday -> vacationRepository.save(holiday) }
      .transform { v -> ServerResponse.ok().body(v, Vacation::class.java) }
}