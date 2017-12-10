package com.tooling.core.service

import com.tooling.core.exception.InvalidTenantIdException
import com.tooling.core.exception.InvalidUserGroupException
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class HeaderReader {
  companion object {
    fun getTenantId(req: ServerRequest) =
      Mono
        .just(req.headers().header("tenantId"))
        .map { tenantIds ->
          if (tenantIds.isEmpty() || tenantIds.get(0) == null || tenantIds.get(0).isEmpty())
            throw InvalidTenantIdException("Tenant id is null")

          if (tenantIds.size > 1)
            throw InvalidTenantIdException("Multiple tenant id found")

          tenantIds.get(0)
        }

    fun getUserId(req: ServerRequest) =
      Mono
        .just(req.headers().header("userId"))
        .map { userIds ->
          if (userIds.isEmpty() || userIds.get(0) == null || userIds.get(0).isEmpty())
            throw InvalidTenantIdException("user id is null")

          if (userIds.size > 1)
            throw InvalidTenantIdException("Multiple user id found")

          userIds.get(0)
        }

    fun oneRuleMatch(groups: Flux<String>, rules: Flux<String>) =
      rules
        .flatMap { rule ->
          extractGroups(groups)
            .flatMap { grp -> Flux.fromIterable(grp) }
            .filter(rule::equals)
        }
        .switchIfEmpty {
          throw InvalidUserGroupException("User is not authorized to go here.")
        }

    private fun extractGroups(groups: Flux<String>) =
      groups
        .map { stringList -> stringList.replace(Regex("\\[|\\]| "), "") }
        .map { stringList -> stringList.split(",") }
  }
}