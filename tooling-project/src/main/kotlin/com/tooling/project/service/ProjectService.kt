package com.tooling.project.service

import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.core.service.HeaderReader
import com.tooling.project.model.Project
import com.tooling.project.model.ProjectDto
import com.tooling.project.repository.ProjectRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ProjectService(private val projectRepository: ProjectRepository) {

  companion object {
    val ADMIN_GROUP = Flux.just("ADMIN", "PROJECT_ADMIN")
  }

  fun insert(projectDto: ProjectDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapProjectDtoToProject(projectDto, request)
          .flatMap(projectRepository::insert)
      )

  fun update(projectDto: ProjectDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapProjectDtoToProject(projectDto, request)
          .flatMap(projectRepository::update)
      )

  fun delete(tenantId: Mono<String>, id: Mono<String>, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(projectRepository.deleteByCodeAndTenantId(id, tenantId))

  private fun mapProjectDtoToProject(projectDto: ProjectDto, request: ServerRequest) =
    HeaderReader.getTenantId(request)
      .map { tenantId ->
        Project(projectDto.id,projectDto.code, projectDto.name, projectDto.description,
          projectDto.color, projectDto.status, tenantId)
      }
}