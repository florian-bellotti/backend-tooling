package com.tooling.activity.handler

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.ActivityDto
import com.tooling.activity.repository.ActivityRepository
import com.tooling.activity.service.ActivityService
import com.tooling.core.service.HeaderReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ActivityHandler(private val activityService: ActivityService,
                      private val activityRepository: ActivityRepository) {

  fun find(request: ServerRequest) =
    Mono
      .just(request)
      .flatMap(HeaderReader.Companion::getTenantId)
      .transform { tenantId ->
        ServerResponse.ok().body(
          activityRepository
            .find(tenantId, request.queryParams())
            .map { activity -> ActivityDto(activity) }, ActivityDto::class.java)
      }

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ActivityDto::class.java)
      .flatMap { activity ->
        activityService.insert(activity, request, Flux.fromIterable(request.headers().header("grp")))
      }
      .transform { createdActivity ->
        ServerResponse.ok().body(createdActivity
          .map { activity -> ActivityDto(activity) }, ActivityDto::class.java) }

  fun update(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ActivityDto::class.java)
      .flatMap { project ->
        activityService.update(project, request, Flux.fromIterable(request.headers().header("grp")))
      }
      .transform { result -> ServerResponse.ok().body(result, UpdateResult::class.java) }

  fun delete(request: ServerRequest): Mono<ServerResponse> =
    Mono
      .just(request)
      .flatMap { req ->
        activityService.delete(
          HeaderReader.getTenantId(req),
          Mono.just(req.pathVariable("id")),
          Flux.fromIterable(req.headers().header("grp"))
        )
      }
      .transform { result: Mono<DeleteResult> -> ServerResponse.ok().body(result, DeleteResult::class.java) }
}