package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.RoleEntity
import java.security.Principal
import java.time.OffsetDateTime
import javax.validation.constraints.NotBlank

data class Role (
    val id: Long?=null,
    val roleName: @NotBlank String,
    val description: String?,
    val created: OffsetDateTime?,
    val lastModified: OffsetDateTime?
) : Principal {


    fun convert() = RoleEntity(id, roleName, description);


    companion object Factory {
        fun convert(role: RoleEntity): Role {
            return Role(role.id, role.roleName, role.description, role.created, role.lastModified);
        }
    }

    override fun getName(): String {
        return roleName;
    }

}
