package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.LanguageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LanguageDao: JpaRepository<LanguageEntity, Long> {
}
