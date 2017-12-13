package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.ActivityType
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ActivityTypeRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ActivityTypeRepositoryCustom {

  override fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<ActivityType> {
    val query = Query(Criteria.where("tenantId").`is`(tenantId.block()))

    for (queryField in queryFields.entries) {
      query.addCriteria(Criteria.where(queryField.key).`in`(queryField.value))
    }

    return reactiveMongoTemplate.find(query, ActivityType::class.java)
  }

  override fun update(type: ActivityType): Mono<UpdateResult> {
    val query = Query(Criteria
      .where("id").`is`(type.id)
      .and("tenantId").`is`(type.tenantId))
    val update = Update()
    update.set("name", type.name)
    return reactiveMongoTemplate.updateFirst(query, update, ActivityType::class.java)
  }

  override fun deleteByIdAndTenantId(id: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult> {
    val query = Query(Criteria
      .where("id").`is`(id.block())
      .and("tenantId").`is`(tenantId.block()))
    return reactiveMongoTemplate.remove(query, ActivityType::class.java)
  }
}