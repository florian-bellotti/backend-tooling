package com.tooling.user.config

import com.tooling.user.security.AuthTokenFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.StaticHeadersWriter
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class CustomWebSecurityConfigurerAdapter(val xAuthTokenFilter: AuthTokenFilter)
  : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.headers()
      .addHeaderWriter(XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
      .addHeaderWriter(StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
      .addHeaderWriter(StaticHeadersWriter("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"))
      .addHeaderWriter(StaticHeadersWriter("Access-Control-Allow-Headers", "Content-Type, Accept, X-Auth-Token, Authorization"))
      .addHeaderWriter(StaticHeadersWriter("Access-Control-Max-Age", "1800"))

    http.logout().disable()
    http.csrf().disable()
    http.addFilterBefore(xAuthTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

    http.authorizeRequests()
      .antMatchers(GET, "/health").anonymous()
      .antMatchers(GET, "/info").anonymous()
      .antMatchers(POST, "/users/authenticate").anonymous()
      //.antMatchers("/**").authenticated()
  }
}
