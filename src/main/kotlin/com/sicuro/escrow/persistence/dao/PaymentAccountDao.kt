package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentAccountDao : JpaRepository<PaymentAccountEntity, Long>
