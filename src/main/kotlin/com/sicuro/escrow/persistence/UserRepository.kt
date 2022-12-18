package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.SecurityQuestionDto
import com.sicuro.escrow.model.User
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.entity.UserEntity
import com.sicuro.escrow.util.security.PasswordGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserRepository @Autowired constructor(
    val userDao: UserDao,
    val rolesDao: RoleDao,
    val activationLinkRepository: ActivationLinkRepository,
    val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createUser(username: String, password: String, customerId: Long, roles: List<String>, status: BaseStatus = BaseStatus.deactivated): Pair<User, String> {
        val roleEntities = rolesDao.findByRoleNameIn(roles)
        val userEntity = userDao.save(
            UserEntity(
                null,
                username,
                password,
                status,
                customerId,
                roleEntities
            )
        )

        val activationLinkId = activationLinkRepository.createLink(userEntity, LinkType.ACCOUNT_ACTIVATION)

        return Pair(User.convert(userEntity), activationLinkId)
    }

    @Transactional
    fun changePassword(username: String, password: String) {
        val user = getUser(username)
        val encodedPassword = passwordEncoder.encode(password)

        val updatedUser = user.copy(
            password = encodedPassword,
            status = BaseStatus.active
        )
        userDao.save(updatedUser)
    }

    @Transactional
    fun activateUser(username: String) {
        userDao.findByUsername(username)?.also { user ->
            user.status = BaseStatus.active
            userDao.save(user)
        } ?: throw RuntimeException("Could not resolve user in validated activation link")
    }

    @Transactional
    fun updateUserSequrityQuestion(securityQuestionDto: SecurityQuestionDto, username: String) {
        val user = getUser(username)
        val encodeAnswer = passwordEncoder.encode(securityQuestionDto.answer)
        val updatedUser = user.copy(
            securityQuestion = securityQuestionDto.question,
            securityQuestionAnswer = encodeAnswer
        )
        userDao.save(updatedUser)
    }

    @Transactional
    fun changeUsername(oldUsername: String, newUsername: String) {
        userDao.findByUsername(oldUsername)?.also { user ->
            user.username = newUsername
            userDao.save(user)
        } ?: throw RuntimeException("Could not resolve user in validated activation link")
    }

    @Transactional
    fun initiateResetPassword(username: String): String {
        val user = getUser(username)
        return activationLinkRepository.createLink(
            user,
            LinkType.RESET_PASSWORD
        )
    }

    @Transactional
    fun resetPassword(username: String): String {
        val user = getUser(username)
        val passwordGenerator = PasswordGenerator()
        val password = passwordGenerator.generate(8)
        val updatedUser = user.copy(password = passwordEncoder.encode(password), status = BaseStatus.temporary_password)
        userDao.save(updatedUser)
        return password
    }

    fun findByUsername(username: String): User? {
        return userDao.findByUsername(username)?.let {
            User.convert(it)
        }
    }

    fun validatePassword(username: String, password: String): Boolean {
        return passwordEncoder.matches(password, userDao.findByUsername(username)?.password)
    }

    private fun getUser(username: String): UserEntity {
        return userDao.findByUsername(username) ?: let {
            throw ObjectNotFoundException("user does not exist")
        }
    }
}
