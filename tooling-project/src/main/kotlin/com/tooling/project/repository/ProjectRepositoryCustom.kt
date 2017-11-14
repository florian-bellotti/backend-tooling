package com.tooling.project.repository

import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import reactor.core.publisher.Mono

interface ProjectRepositoryCustom {
  fun update(project: Project): Mono<UpdateResult>
}