package com.tooling.project.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ProjectRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ProjectRepositoryCustom {

  override fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<Project> {
    val query = Query(Criteria.where("tenantId").`is`(tenantId.block()))

    for (queryField in queryFields.entries) {
      query.addCriteria(Criteria.where(queryField.key).`in`(queryField.value))
    }

    return reactiveMongoTemplate.find(query, Project::class.java)
  }

  override fun update(project: Project): Mono<UpdateResult> {
    val query = Query(Criteria
      .where("code").`is`(project.code)
      .and("tenantId").`is`(project.tenantId))
    val update = Update()
    update.set("name", project.name)
    update.set("status", project.status)
    return reactiveMongoTemplate.updateFirst(query, update, Project::class.java)
  }

  override fun deleteByCodeAndTenantId(code: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult> {
    val query = Query(Criteria
      .where("code").`is`(code.block())
      .and("tenantId").`is`(tenantId.block()))
    return reactiveMongoTemplate.remove(query, Project::class.java)
  }
}