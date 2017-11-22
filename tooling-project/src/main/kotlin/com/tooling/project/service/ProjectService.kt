package com.tooling.project.service

import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.project.model.Project
import com.tooling.project.model.ProjectDto
import com.tooling.project.repository.ProjectRepository
import com.tooling.tenant.exception.InvalidTenantIdException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ProjectService(private val projectRepository: ProjectRepository) {

  companion object {
    val ADMIN_GROUP = Flux.just("ADMIN", "USER_ADMIN")
  }

  fun getTenantIdFromHeader(req: ServerRequest) =
    Mono
      .just(req.headers().header("tenantId"))
      .map { tenantIds ->
        if (tenantIds.isEmpty() || tenantIds.get(0) == null || tenantIds.get(0).isEmpty())
          throw InvalidTenantIdException("Tenant id is null")

        if (tenantIds.size > 1)
          throw InvalidTenantIdException("Multiple tenant id found")

        tenantIds.get(0)
      }

  fun insert(projectDto: ProjectDto, request: ServerRequest, groups: Flux<String>) =
    oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapProjectDtoToProject(projectDto, request)
          .flatMap(projectRepository::insert)
      )

  fun update(projectDto: ProjectDto, request: ServerRequest, groups: Flux<String>) =
    oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapProjectDtoToProject(projectDto, request)
          .flatMap(projectRepository::update)
      )

  fun delete(tenantId: Mono<String>, code: Mono<String>, groups: Flux<String>) =
    oneRuleMatch(groups, ADMIN_GROUP)
      .then(projectRepository.deleteByCodeAndTenantId(code, tenantId))

  private fun mapProjectDtoToProject(projectDto: ProjectDto, request: ServerRequest) =
    getTenantIdFromHeader(request)
      .map { tenantId -> Project(projectDto.code, projectDto.name, projectDto.status, tenantId) }

  private fun oneRuleMatch(groups: Flux<String>, rules: Flux<String>) =
    rules
      .flatMap { rule ->
        extractGroups(groups)
          .flatMap { grp -> Flux.fromIterable(grp) }
          .filter(rule::equals)
      }
      .switchIfEmpty {
        throw InvalidUserGroupException("User is not authorized to go here.")
      }

  private fun extractGroups(groups: Flux<String>) =
    groups
      .map { stringList -> stringList.replace(Regex("\\[|\\]| "), "") }
      .map { stringList -> stringList.split(",") }
}