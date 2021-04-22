package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.UserEntity
import kotlin.streams.toList

data class User(
    val username: String,
    val status: BaseStatus,
    val roles: List<String>,
    val securityQuestion: SecurityQuestion?= null
){
    companion object Factory {
        fun convert(user: UserEntity) = User(user.username, user.status, user.roles.stream().map { r -> Role.convert(r).roleName }.toList(), user.securityQuestion);
    }
}
