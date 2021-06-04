package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.CountryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CountryDao: JpaRepository<CountryEntity, Long> {

    fun findByIso(iso:String): CountryEntity?
}
