package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CustomerDao: JpaRepository<CustomerEntity, Long>, JpaSpecificationExecutor<CustomerEntity> {

    fun countByCustomerNumber(customerNumber:String): Long

    fun findByEmail(email: String): CustomerEntity?

}
