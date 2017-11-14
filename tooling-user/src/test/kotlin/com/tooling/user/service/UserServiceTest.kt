package com.tooling.user.service

import com.nhaarman.mockito_kotlin.*
import com.tooling.tenant.service.TenantService
import com.tooling.user.model.User
import com.tooling.user.respository.UserRepository
import com.tooling.user.sample.TenantSample
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.internal.verification.Times

class UserServiceTest {

  @Test
  fun insertDefaultUsers() {
    // Given
    val count: Long = 0
    val tenantService = mock<TenantService> {
      on { findAll() } doReturn TenantSample.TENANTS_MONO
    }
    val userRepository = mock<UserRepository> {
      on { countByTenantId(any()) } doReturn count
    }
    val userService = UserService(userRepository, tenantService)

    // When
    userService.insertDefaultUsers()

    // Then
    argumentCaptor<User>().apply {
      verify(userRepository, Times(2)).countByTenantId(any())
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
    val tenantService = mock<TenantService> {
      on { findAll() } doReturn TenantSample.TENANTS_MONO
    }
    val userRepository = mock<UserRepository> {
      on { countByTenantId(any()) } doReturn count
    }
    val userService = UserService(userRepository, tenantService)

    // When
    userService.insertDefaultUsers()

    // Then
    argumentCaptor<User>().apply {
      verify(userRepository, Times(2)).countByTenantId(any())
      verify(userRepository, never()).insert(capture())

    }
  }
}