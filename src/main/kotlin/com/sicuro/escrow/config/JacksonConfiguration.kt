package com.sicuro.escrow.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class JacksonConfiguration {

/*    @Bean
    @Primary // overrides default object mapper in SpringBoot context
    fun objectMapper(): ObjectMapper? {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(Jdk8Module())
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        // pretty-print data
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)

        // required by some enums in KDI model deserialization
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)

        // IMPORTANT: has to be set to false for JavaTimeModule() to work correctly!
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        *//**
         * This has to be explicitly disabled, otherwise Jackson will lose time zone offsets
         * when deserializing OffsetDateTime instances
         *//*
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
        return objectMapper
    }*/
}
