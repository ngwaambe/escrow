package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserDao : JpaRepository<UserEntity, Long> {

    fun findByUsername(username: String): UserEntity?

    fun findByUsernameAndStatusIn(username: String, status: List<BaseStatus>): UserEntity?
}
