package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.User
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.entity.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserRepository @Autowired constructor(
    val userDao:  UserDao,
    val rolesDao: RoleDao,
    val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createUser(username: String, password: String, roles: List<String>): User {
        val roleEntities = rolesDao.findByRoleNameIn(roles)
        val userEntity =  userDao.save(UserEntity(
           null,
           username,
           password,
           BaseStatus.deactivated,
           roleEntities
       ))
       return User.convert(userEntity)
    }

    fun changePassword(username:String, password: String) {
        val user = userDao.findByUsername(username)?:let {
            throw ObjectNotFoundException("user does not exist")
        }
        val encodedPassword = passwordEncoder.encode(password)

        val updatedUser = user.copy(
            password = encodedPassword
        )
        userDao.save(updatedUser)

    }
}
