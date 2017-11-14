package com.tooling.project.service

import com.mongodb.client.result.UpdateResult
import com.tooling.project.exception.InvalidProjectException
import com.tooling.project.model.Project
import com.tooling.project.model.ProjectDto
import com.tooling.project.repository.ProjectRepository
import com.tooling.tenant.exception.InvalidTenantIdException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

@Component
class ProjectService(private val projectRepository: ProjectRepository) {

  fun getTenantIdFromHeader(req: ServerRequest): String {
    val tenantIds = req.headers().header("tenantId")
    if (tenantIds.isEmpty() || tenantIds.get(0) == null)
      throw InvalidTenantIdException("Tenant id is null")

    if (tenantIds.size > 1)
      throw InvalidTenantIdException("Multiple tenant id found")

    return tenantIds.get(0)!!
  }

  fun insert(projectDto: ProjectDto?, request: ServerRequest): Mono<Project> {
    checkProjectDto(projectDto)
    val project = mapProjectDtoToProject(projectDto!!, request)
    return projectRepository.insert(project)
  }

  fun update(projectDto: ProjectDto?, request: ServerRequest): Mono<UpdateResult> {
    checkProjectDto(projectDto)
    val project = mapProjectDtoToProject(projectDto!!, request)
    return projectRepository.update(project)
  }

  private fun checkProjectDto(projectDto: ProjectDto?) {
    if (projectDto == null)
      throw InvalidProjectException("Project is null")
  }

  private fun mapProjectDtoToProject(projectDto: ProjectDto, request: ServerRequest): Project {
    val tenantId = getTenantIdFromHeader(request)
    return Project(projectDto.code, projectDto.name, projectDto.status, tenantId)
  }
}