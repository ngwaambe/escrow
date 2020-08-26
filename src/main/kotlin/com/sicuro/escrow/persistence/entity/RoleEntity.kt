package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.security.Principal
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

/**
 * Defines Roles which a user in system can have.
 *
 * @since 1.0.0
 * @author engwaambe
 */
@Entity
@Table(name = "roles", indexes = [
    Index(columnList = "name", name = "idx_name_roles"),
    Index(columnList = "description", name = "idx_description_roles")
])
class RoleEntity constructor(
    id:Long?,
    /**
     * Role name, unique identifier
     */
    @Column(nullable = false, length = 80, unique = true, name = "name")
    var roleName: String,

    /**
     * Describes role.
     */
    @Column(nullable = false)
    var description: String? = null,

    created: OffsetDateTime,

    lastModified: OffsetDateTime

):BaseEntity(id, created, lastModified){
    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is RoleEntity) return false
        if (!super.equals(obj)) return false

        if (roleName != obj.roleName) return false
        if (description != obj.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + roleName.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}

