package com.tooling.user.service

import com.nhaarman.mockito_kotlin.*
import com.tooling.instance.service.InstanceService
import com.tooling.user.jackson.YamlObjectMapper
import com.tooling.user.model.User
import com.tooling.user.respository.UserRepository
import com.tooling.user.sample.InstanceSample
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.internal.verification.Times

class UserServiceTest {

  @Test
  fun insertDefaultUsers() {
    // Given
    val count: Long = 0
    val instanceService = mock<InstanceService> {
      on { findAll() } doReturn InstanceSample.INSTANCES_MONO
    }
    val userRepository = mock<UserRepository> {
      on { countByInstanceId(any()) } doReturn count
    }
    val userService = UserService(userRepository, instanceService)

    // When
    userService.insertDefaultUsers()

    // Then
    argumentCaptor<User>().apply {
      verify(userRepository, Times(2)).countByInstanceId(any())
      verify(userRepository, Times(2)).insert(capture())

      assertThat(firstValue).describedAs("User").isNotNull()
      assertThat(firstValue.active).describedAs("User Active value").isTrue()
      assertThat(firstValue.email).describedAs("User Email").isEqualTo("admin@tooling.ovh")
    }
  }

  @Test
  fun `dont create user`() {
    // Given
    val count: Long = 2
    val instanceService = mock<InstanceService> {
      on { findAll() } doReturn InstanceSample.INSTANCES_MONO
    }
    val userRepository = mock<UserRepository> {
      on { countByInstanceId(any()) } doReturn count
    }
    val userService = UserService(userRepository, instanceService)

    // When
    userService.insertDefaultUsers()

    // Then
    argumentCaptor<User>().apply {
      verify(userRepository, Times(2)).countByInstanceId(any())
      verify(userRepository, never()).insert(capture())

    }
  }
}