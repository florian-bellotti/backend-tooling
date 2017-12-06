package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.Activity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.temporal.TemporalAccessor
import java.util.*

class ActivityRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ActivityRepositoryCustom {

  override fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<Activity> {
    val query = Query(Criteria.where("tenantId").`is`(tenantId.block()))

    for (queryField in queryFields.entries) {
      if ("start".equals(queryField.key)) {
        if (queryField.value.size == 1) {
          val start = Instant.ofEpochSecond(queryField.value[0].toLong())
          query.addCriteria(Criteria.where("startDate").gte(start))
        }
      } else if ("end".equals(queryField.key)) {
        if (queryField.value.size == 1) {
          val end = Instant.ofEpochSecond(queryField.value[0].toLong())
          query.addCriteria(Criteria.where("endDate").lte(end))
        }
      } else {
        query.addCriteria(Criteria.where(queryField.key).`in`(queryField.value))
      }
    }

    return reactiveMongoTemplate.find(query, Activity::class.java)
  }

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