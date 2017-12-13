package com.tooling.activity.handler

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.ActivityType
import com.tooling.activity.model.ActivityTypeDto
import com.tooling.activity.repository.ActivityTypeRepository
import com.tooling.activity.service.ActivityTypeService
import com.tooling.core.service.HeaderReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ActivityTypeHandler(private val activityTypeService: ActivityTypeService,
                          private val activityTypeRepository: ActivityTypeRepository) {

  fun find(request: ServerRequest) =
    Mono
      .just(request)
      .flatMap(HeaderReader.Companion::getTenantId)
      .transform { tenantId ->
        ServerResponse.ok().body(
          activityTypeRepository
            .find(tenantId, request.queryParams()), ActivityType::class.java)
      }

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ActivityTypeDto::class.java)
      .flatMap { typeDto ->
        activityTypeService.insert(
          typeDto,
          request,
          Flux.fromIterable(request.headers().header("grp"))
        )
      }
      .transform { createdType ->
        ServerResponse.ok().body(createdType
          .map { type -> ActivityTypeDto(type) }, ActivityTypeDto::class.java) }

  fun update(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ActivityTypeDto::class.java)
      .flatMap { typeDto ->
        activityTypeService.update(
          typeDto,
          request,
          Flux.fromIterable(request.headers().header("grp"))
        )
      }
      .transform { result -> ServerResponse.ok().body(result, UpdateResult::class.java) }

  fun delete(request: ServerRequest): Mono<ServerResponse> =
    Mono
      .just(request)
      .flatMap { req ->
        activityTypeService.delete(
          HeaderReader.getTenantId(req),
          Mono.just(req.pathVariable("id")),
          Flux.fromIterable(req.headers().header("grp"))
        )
      }
      .transform { result: Mono<DeleteResult> -> ServerResponse.ok().body(result, DeleteResult::class.java) }
}