package com.tooling.project.handler

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.core.service.HeaderReader
import com.tooling.project.model.ProjectDto
import com.tooling.project.repository.ProjectRepository
import com.tooling.project.service.ProjectService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ProjectHandler(private val projectService: ProjectService,
                     private val projectRepository: ProjectRepository) {

  fun find(request: ServerRequest): Mono<ServerResponse> =
    Mono
      .just(request)
      .flatMap(HeaderReader.Companion::getTenantId)
      .transform { tenantId ->
        ServerResponse.ok().body(
          projectRepository
            .find(tenantId, request.queryParams())
            .map { project -> ProjectDto(project) }, ProjectDto::class.java)
      }

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ProjectDto::class.java)
      .flatMap { project ->
        projectService.insert(project, request, Flux.fromIterable(request.headers().header("grp")))
      }
      .transform { createdProject ->
        ServerResponse.ok().body(createdProject
          .map { project -> ProjectDto(project) }, ProjectDto::class.java) }

  fun update(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ProjectDto::class.java)
      .flatMap { project ->
        projectService.update(project, request, Flux.fromIterable(request.headers().header("grp")))
      }
      .transform { result -> ServerResponse.ok().body(result, UpdateResult::class.java) }

  fun delete(request: ServerRequest): Mono<ServerResponse> =
    Mono
      .just(request)
      .flatMap { req ->
        projectService.delete(
          HeaderReader.getTenantId(req),
          Mono.just(req.pathVariable("code")),
          Flux.fromIterable(req.headers().header("grp"))
        )
      }
      .transform { result: Mono<DeleteResult> -> ServerResponse.ok().body(result, DeleteResult::class.java) }
}