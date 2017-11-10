package com.tooling.user.jackson

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

open class YamlObjectMapper : ObjectMapper(YAMLFactory()) {
  init {
    this.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    this.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    this.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    this.registerModule(JavaTimeModule())
    this.registerModule(KotlinModule())
  }
}
