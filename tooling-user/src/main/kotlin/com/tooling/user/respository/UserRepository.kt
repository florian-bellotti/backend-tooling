package com.tooling.user.respository

import com.tooling.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>, UserRepositoryCustom {
  fun findByEmailAndTenantId(email: String, tenantId: String): User?
  fun findByIdAndTenantId(id: String, tenantId: String): User?
  fun countByTenantId(tenantId: String): Long
}
