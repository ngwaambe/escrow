package com.sicuro.escrow.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.time.OffsetDateTime
import java.util.*


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "auditingDateTimeProvider")
class JpaConfig {

    @Bean
    fun auditorAware() = AuditorAwareImpl()

    @Bean // Makes OffsetDateTime compatible with auditing fields
    fun auditingDateTimeProvider() = DateTimeProvider { Optional.of(OffsetDateTime.now()) }
}


class AuditorAwareImpl: AuditorAware<String>{

    override fun getCurrentAuditor(): Optional<String> {
        SecurityContextHolder.getContext().authentication?.let {
            return Optional.of(it.name)
        }?: return Optional.empty<String>()
    }
}
