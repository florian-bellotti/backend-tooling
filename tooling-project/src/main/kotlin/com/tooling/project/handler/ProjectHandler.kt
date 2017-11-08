package com.tooling.project.handler

import com.tooling.project.model.Project
import com.tooling.project.repository.ProjectRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProjectHandler(private val projectRepository: ProjectRepository) {

  fun find(): Mono<ServerResponse> =
    ServerResponse.ok().body(projectRepository.findAll(), Project::class.java)

  fun create(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono(Project::class.java)
      .flatMap(projectRepository::insert)
      .transform { project -> ServerResponse.ok().body(project, Project::class.java) }
}