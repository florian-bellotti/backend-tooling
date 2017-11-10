package com.tooling.user.service

import com.tooling.instance.model.Instance
import com.tooling.instance.service.InstanceService
import com.tooling.user.jackson.YamlObjectMapper
import com.tooling.user.model.TmpUser
import com.tooling.user.model.User
import com.tooling.user.respository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCrypt.gensalt
import org.springframework.security.crypto.bcrypt.BCrypt.hashpw
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

data class UsersContainer(var users: MutableList<TmpUser>)

@Service
open class UserService(private val userRepository: UserRepository,
                       private val instanceService: InstanceService) {

  private val yamlObjectMapper: YamlObjectMapper

  init {
    yamlObjectMapper = YamlObjectMapper()
  }

  companion object {
    val logger = LoggerFactory.getLogger(UserService::class.java)
  }

  @PostConstruct
  fun insertDefaultUsers() =
    instanceService
      .findAll()
      .subscribe(this::insertDefaultUsersByInstance)

  fun insertDefaultUsersByInstance(instance: Instance) {
    val count = userRepository.countByInstanceId(instance.id!!)

    if (count <= 0) {
      val usersStream = UserService::class.java.getResourceAsStream("defaultUsers.yml")
      yamlObjectMapper
        .readValue(usersStream, UsersContainer::class.java)
        .users.onEach {
          it.password = hashpw(it.password, gensalt())

          val user = userRepository.insert(User(null, it.email, it.password, it.firstName, it.lastName, it.groups,
            it.address, it.phone, it.active, it.locale, it.activationDate, it.creationDate, instance.id!!))
          logger.info("user {} created", it)}
        }
    }
}
