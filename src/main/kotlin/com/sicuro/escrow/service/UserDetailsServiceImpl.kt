package com.sicuro.escrow.service

import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.persistence.ActivationLinkRepository
import com.sicuro.escrow.persistence.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

class MyUser(
    username: String,
    password: String,
    grantedAuthorities: HashSet<GrantedAuthority>,
    val customerId: Long,
    val hasSecurityQuestion: Boolean,
    val temporaryPassword: Boolean
) : User(username, password, grantedAuthorities)

@Service
class UserDetailsServiceImpl @Autowired constructor(val userDao: UserDao, val linkRepository: ActivationLinkRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): MyUser {
        userDao.findByUsernameAndStatusIn(username, listOf(BaseStatus.active, BaseStatus.temporary_password))?.let { user ->
            val grantedAuthorities = HashSet<GrantedAuthority>()
            user.roles.forEach { grantedAuthorities.add(SimpleGrantedAuthority(it.roleName)) }
            return MyUser(
                user.username,
                user.password,
                grantedAuthorities,
                user.customerId,
                user.hasSecurityQuestion(),
                user.status == BaseStatus.temporary_password
            )
        } ?: throw UsernameNotFoundException("login.error.userNotFound")
    }
}
