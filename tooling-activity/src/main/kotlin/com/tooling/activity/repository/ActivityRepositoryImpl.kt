package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.Activity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Mono

class ActivityRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ActivityRepositoryCustom {
  override fun update(activity: Activity): Mono<UpdateResult> {
    val query = Query(Criteria
      .where("id").`is`(activity.id)
      .and("tenantId").`is`(activity.tenantId))
    val update = Update()
    update.set("startDate", activity.startDate)
    update.set("endDate", activity.endDate)
    update.set("code", activity.code)
    update.set("comment", activity.comment)
    return reactiveMongoTemplate.updateFirst(query, update, Activity::class.java)
  }

  override fun deleteByIdAndTenantId(id: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult> {
    val query = Query(Criteria
      .where("id").`is`(id.block())
      .and("tenantId").`is`(tenantId.block()))
    return reactiveMongoTemplate.remove(query, Activity::class.java)
  }
}