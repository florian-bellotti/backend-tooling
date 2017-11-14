package com.tooling.project.repository

import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Mono

class ProjectRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ProjectRepositoryCustom {
  override fun update(project: Project): Mono<UpdateResult> {
    val query = Query(Criteria
      .where("code").`is`(project.code)
      .and("tenantId").`is`(project.tenantId))
    val update = Update()
    update.set("name", project.name)
    update.set("status", project.status)
    return reactiveMongoTemplate.updateFirst(query, update, Project::class.java)
  }
}