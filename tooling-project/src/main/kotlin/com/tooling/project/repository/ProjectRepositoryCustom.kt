package com.tooling.project.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import reactor.core.publisher.Mono

interface ProjectRepositoryCustom {
  fun update(project: Project): Mono<UpdateResult>
  fun deleteByCodeAndTenantId(code: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult>
}