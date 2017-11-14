package com.tooling.project.handler

import com.tooling.project.model.Project
import com.tooling.project.model.ProjectRequest
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
        ServerResponse.ok().body(projectRepository.findByTenantId(tenantId), Project::class.java)
      }

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(ProjectRequest::class.java)
      .flatMap { project -> projectService.insert(project, request) }
      .transform { createdProject -> ServerResponse.ok().body(createdProject, Project::class.java) }
}