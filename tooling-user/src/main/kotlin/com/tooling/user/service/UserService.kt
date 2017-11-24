package com.tooling.user.service

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.tenant.model.Tenant
import com.tooling.tenant.service.TenantService
import com.tooling.user.jackson.YamlObjectMapper
import com.tooling.user.model.TmpUser
import com.tooling.user.model.User
import com.tooling.user.model.UserDto
import com.tooling.user.respository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCrypt.gensalt
import org.springframework.security.crypto.bcrypt.BCrypt.hashpw
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

data class UsersContainer(var users: MutableList<TmpUser>)

@Service
open class UserService(private val userRepository: UserRepository,
                       private val tenantService: TenantService) {

  companion object {
    val logger = LoggerFactory.getLogger(UserService::class.java)!!
    val ADMIN_GROUP = setOf("ADMIN", "USER_ADMIN")
  }

  private val yamlObjectMapper = YamlObjectMapper()

  @PostConstruct
  fun insertDefaultUsers() =
    tenantService
      .findAll()
      .switchIfEmpty { tenantService.insertDefault().subscribe(this::insertDefaultUsersByInstance) }
      .subscribe(this::insertDefaultUsersByInstance)

  private fun insertDefaultUsersByInstance(tenant: Tenant) {
    val count = userRepository.countByTenantId(tenant.id!!)

    if (count <= 0) {
      val usersStream = UserService::class.java.getResourceAsStream("defaultUsers.yml")
      yamlObjectMapper
        .readValue(usersStream, UsersContainer::class.java)
        .users.onEach {
        it.password = hashpw(it.password, gensalt())

        val user = userRepository.insert(User(null, it.email, it.password, it.firstName, it.lastName, it.groups,
          it.address, it.phone, it.active, it.locale, it.activationDate, it.creationDate, tenant.id!!))
        logger.info("user {} created", user)
      }
    }
  }

  fun find(tenantId: String, allRequestParams: Map<String, String>): List<UserDto> {
    val users = userRepository.find(tenantId, allRequestParams)
    val usersDto = ArrayList<UserDto>()
    users.mapTo(usersDto) { UserDto(it) }
    return usersDto
  }

  fun create(user: TmpUser, tenantId: String, groups: String): User {
    oneRuleMatch(groups, ADMIN_GROUP)
    user.password = hashpw(user.password, gensalt())
    return userRepository.insert(User(null, user.email, user.password, user.firstName, user.lastName, user.groups,
      user.address, user.phone, user.active, user.locale, user.activationDate, user.creationDate, tenantId))
  }

  fun update(user: UserDto, tenantId: String, groups: String): UpdateResult {
    oneRuleMatch(groups, ADMIN_GROUP)
    return userRepository.update(tenantId, user)
  }

  fun delete(id: String, tenantId: String, groups: String): DeleteResult {
    oneRuleMatch(groups, ADMIN_GROUP)
    return userRepository.deleteByIdAndTenantId(id, tenantId)
  }

  private fun oneRuleMatch(groupsString: String, rules: Set<String>) {
    val groups = extractGroups(groupsString)

    val contains = groups.any { rules.contains(it) }
    if (!contains)
      throw InvalidUserGroupException("User is not authorized to go here.")
  }

  private fun extractGroups(groups: String): List<String> {
    val tmpGroup = groups.replace(Regex("\\[|\\]| "), "")
    return tmpGroup.split(",")
  }
}
