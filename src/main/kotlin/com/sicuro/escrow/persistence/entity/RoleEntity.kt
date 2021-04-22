package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.AccessType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
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
@EntityListeners(AuditingEntityListener::class)
data class RoleEntity constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,
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

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
)

