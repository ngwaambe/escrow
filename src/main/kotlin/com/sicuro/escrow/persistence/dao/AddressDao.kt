package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository

interface
AddressDao: JpaRepository<AddressEntity, Long> {
}
