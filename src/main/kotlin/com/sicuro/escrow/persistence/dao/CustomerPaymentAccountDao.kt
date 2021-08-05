package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.CustomerPaymentAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerPaymentAccountDao:JpaRepository<CustomerPaymentAccountEntity, Long> {

    fun countByCustomerId(customerId: Long): Long

    fun findAllByCustomerId(customerId: Long): List<CustomerPaymentAccountEntity>
}
