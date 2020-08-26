package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.BaseBoStatus
import com.sicuro.escrow.model.SecurityQuestion
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.CreatedDate

import java.util.*
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
class UserEntity constructor(

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: BaseBoStatus,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id", unique = false)], inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id", unique = false)], uniqueConstraints = [UniqueConstraint(name = "idx_userId_roleId", columnNames = ["role_id", "user_id"])])
    var roles: Set<RoleEntity>,


    @Column(name = "contact_id")
    var contactId: Long? = null,

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
    var lastModifiedBy: String? = null

): BaseEntity() {
    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is UserEntity) return false
        if (!super.equals(obj)) return false

        if (username != obj.username) return false
        if (password != obj.password) return false
        if (status != obj.status) return false
        if (roles != obj.roles) return false
        if (contactId != obj.contactId) return false
        if (securityQuestion != obj.securityQuestion) return false
        if (securityQuestionAnswer != obj.securityQuestionAnswer) return false
        if (createdBy != obj.createdBy) return false
        if (lastModifiedBy != obj.lastModifiedBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + roles.hashCode()
        result = 31 * result + (contactId?.hashCode() ?: 0)
        result = 31 * result + (securityQuestion?.hashCode() ?: 0)
        result = 31 * result + (securityQuestionAnswer?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (lastModifiedBy?.hashCode() ?: 0)
        return result
    }
}
