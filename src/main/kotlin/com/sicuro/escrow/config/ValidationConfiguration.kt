package com.sicuro.escrow.config

import org.hibernate.validator.HibernateValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import javax.validation.Validation
import javax.validation.Validator

@Configuration
class ValidationConfiguration {
    @Bean
    fun validatorFactory(): Validator {
        return Validation.byProvider(HibernateValidator::class.java)
            .configure()
            .temporalValidationTolerance(Duration.ofMinutes(30))
            .buildValidatorFactory().validator
    }
}
