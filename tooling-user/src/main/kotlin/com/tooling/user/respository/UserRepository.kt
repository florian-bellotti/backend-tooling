package com.tooling.user.respository

import com.tooling.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
  fun findByEmailAndInstanceId(email: String, instanceId: String): User?
  fun countByInstanceId(instanceId: String): Long
}
