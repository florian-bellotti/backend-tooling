package com.tooling.user.respository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.user.model.User
import com.tooling.user.model.UserDto
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

class UserRepositoryImpl(private val mongoTemplate: MongoTemplate): UserRepositoryCustom {
  override fun find(tenantId: String, queryFields: Map<String, String>): List<User> {
    val query = Query(Criteria.where("tenantId").`is`(tenantId))

    for (queryField in queryFields.entries) {
      query.addCriteria(Criteria.where(queryField.key).`in`(queryField.value))
    }

    return mongoTemplate.find(query, User::class.java)
  }

  override fun findAll(usersId: List<String>): List<User> {
    val query = Query(Criteria.where("id").`in`(usersId))
    return mongoTemplate.find(query, User::class.java)
  }

  override fun update(tenantId: String, user: UserDto): UpdateResult {
    val query = Query(Criteria
      .where("id").`is`(user.id)
      .and("tenantId").`is`(tenantId))
    val update = Update()
    update.set("email", user.email)
    update.set("firstName", user.firstName)
    update.set("lastName", user.lastName)
    update.set("groups", user.groups)
    update.set("address", user.address)
    update.set("phone", user.phone)
    update.set("active", user.active)
    update.set("locale", user.locale)
    update.set("workDuration", user.workDuration)
    return mongoTemplate.updateFirst(query, update, User::class.java)
  }

  override fun deleteByIdAndTenantId(id: String, tenantId: String): DeleteResult {
    val query = Query(Criteria
      .where("id").`is`(id)
      .and("tenantId").`is`(tenantId))
    return mongoTemplate.remove(query, User::class.java)
  }
}