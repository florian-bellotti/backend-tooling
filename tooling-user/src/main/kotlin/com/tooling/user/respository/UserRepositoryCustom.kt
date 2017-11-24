package com.tooling.user.respository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.user.model.User
import com.tooling.user.model.UserDto
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepositoryCustom {
  fun find(tenantId: String, queryFields: Map<String,String>): List<User>
  fun update(tenantId: String, user: UserDto): UpdateResult
  fun deleteByIdAndTenantId(id: String, tenantId: String): DeleteResult
}