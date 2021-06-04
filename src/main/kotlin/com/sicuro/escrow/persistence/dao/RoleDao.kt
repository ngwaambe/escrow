package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.persistence.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoleDao: JpaRepository<RoleEntity, Long> {

    fun findByRoleName(name: String): RoleEntity?

    fun findByRoleNameIn(names: List<String>): Set<RoleEntity>
}

