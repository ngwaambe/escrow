package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectAlreadyExistException
import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.Role
import com.sicuro.escrow.persistence.dao.RoleDao
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class RoleRepository(private val roleDao: RoleDao) {

    fun addRole(role: Role): Role {
        roleDao.findByRoleName(role.roleName)?.let {
            throw ObjectAlreadyExistException("Role with name ${role.roleName} already exist. Role name must be unique")
        } ?:  return Role.convert(roleDao.save(role.convert()))
    }

    fun getRole(id: Long): Role {
        val role = roleDao.findById(id).orElseThrow { ObjectNotFoundException("Role does not exist")}
        return Role.convert(role)
    }

    fun getAllRoles(): List<Role> {
        return roleDao.findAll().stream().map { Role.convert(it) }.toList()
    }

    fun deleteRole(id: Long) {
        roleDao.findById(id).ifPresent { roleDao.deleteById(it.id!!) }
    }

    fun findByName(name: String): Role? {
        roleDao.findByRoleName(name)?.let{
            return Role.convert(it)
        }?: throw ObjectNotFoundException("Role does not exist")
    }
}

