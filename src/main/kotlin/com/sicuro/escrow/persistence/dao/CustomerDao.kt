package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerDao: JpaRepository<CustomerEntity, Long> {

    fun countByCustomerNumber(customerNumber:String): Long
}
