package com.tooling.project.service

import com.tooling.project.exception.InvalidProjectException
import com.tooling.project.model.Project
import com.tooling.project.model.ProjectRequest
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

  fun insert(projectReq: ProjectRequest?, request: ServerRequest): Mono<Project> {
    if (projectReq == null)
      throw InvalidProjectException("Project is null")

    val tenantId = getTenantIdFromHeader(request)
    val project = Project(projectReq.code, projectReq.name, projectReq.status, tenantId)

    return projectRepository.insert(project)
  }
}