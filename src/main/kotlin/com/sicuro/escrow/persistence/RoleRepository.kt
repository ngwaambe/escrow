package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectAlreadyExistException
import com.sicuro.escrow.model.Role
import com.sicuro.escrow.persistence.dao.RoleDao
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class RoleRepository(private val roleDao: RoleDao) {

    fun addRole(role: Role): Role {
        if (roleDao.findByRoleName(role.roleName) == null) {
            return Role.convert(roleDao.save(role.convert()));
        }
        throw ObjectAlreadyExistException("Role with name ${role.roleName} already exist. Role name must be unique");
    }

    fun getRole(id: Long): Role {
        val role = roleDao.findById(id).orElseThrow { IllegalStateException()}
        return Role.convert(role)
    }

    fun getAllRoles(): List<Role> {
        return roleDao.findAll().stream().map { Role.convert(it) }.toList()
    }

    fun deleteRole(id: Long) {
        roleDao.findById(id).ifPresent { roleDao.deleteById(it.id!!) }
    }
}

