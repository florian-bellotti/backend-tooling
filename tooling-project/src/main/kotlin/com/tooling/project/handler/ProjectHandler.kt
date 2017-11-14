package com.tooling.project.handler

import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import com.tooling.project.model.ProjectDto
import com.tooling.project.repository.ProjectRepository
import com.tooling.project.service.ProjectService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProjectHandler(private val projectService: ProjectService,
                     private val projectRepository: ProjectRepository) {

  fun find(request: ServerRequest): Mono<ServerResponse> =
    Mono
      .just(request)
      .map(projectService::getTenantIdFromHeader)
      .transform { tenantId ->
        ServerResponse.ok().body(
          projectRepository
            .findByTenantId(tenantId)
            .map { project -> ProjectDto(project) }, ProjectDto::class.java)
      }

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ProjectDto::class.java)
      .flatMap { project -> projectService.insert(project, request) }
      .transform { createdProject ->
        ServerResponse.ok().body(createdProject
          .map { project -> ProjectDto(project) }, ProjectDto::class.java) }

  fun update(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ProjectDto::class.java)
      .flatMap { project -> projectService.update(project, request) }
      .transform { result -> ServerResponse.ok().body(result, UpdateResult::class.java) }
}