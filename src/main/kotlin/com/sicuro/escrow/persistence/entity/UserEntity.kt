package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.SecurityQuestion
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

import javax.persistence.*

/**
 * Business object repressenting user in system.
 *
 * @since 1.0.0
 * @author engwaambe
 */
@Entity
@Table(name = "users", indexes = [
    Index(columnList = "username", name = "idx_username"),
    Index(columnList = "password", name = "idx_password"),
    Index(columnList = "status", name = "idx_status")

])
@EntityListeners(AuditingEntityListener::class)
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: BaseStatus,

    @Column(name = "contact_id", nullable = false)
    var customerId: Long,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    @JoinTable(
        name = "user_roles",
        joinColumns = [
            JoinColumn(name = "user_id",
                referencedColumnName = "id",
                unique = false)],
        inverseJoinColumns = [
            JoinColumn(name = "role_id",
                referencedColumnName = "id",
                unique = false)],
        uniqueConstraints = [
            UniqueConstraint(name = "idx_userId_roleId",
                columnNames = ["role_id", "user_id"])
        ])
    var roles: Set<RoleEntity>,

    @Enumerated(EnumType.STRING)
    @Column(name = "secuirty_question")
    var securityQuestion: SecurityQuestion? = null,


    @Column(name = "security_answer")
    var securityQuestionAnswer: String? = null,

    @CreatedBy
    @Column(name = "created_by", nullable = true)
    var createdBy: String? = null,

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = true)
    var lastModifiedBy: String? = null,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
) {
     fun hasSecurityQuestion() = securityQuestion != null && securityQuestionAnswer != null
}
