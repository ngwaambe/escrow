package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActivationLinkDao: JpaRepository<ActivationLinkEntity, String> {
}
