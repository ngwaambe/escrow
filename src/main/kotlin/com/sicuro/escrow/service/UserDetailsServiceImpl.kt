package com.sicuro.escrow.service

import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.persistence.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl @Autowired constructor(val userDao: UserDao) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        userDao.findByUsernameAndStatus(username, BaseStatus.active)?.let{ user ->
            val grantedAuthorities = HashSet<GrantedAuthority>()
            user.roles.forEach { grantedAuthorities.add( SimpleGrantedAuthority(it.roleName)) }
            return User(user.username, user.password, grantedAuthorities)
        }?: throw UsernameNotFoundException("login.error.userNotFound")
    }
}
