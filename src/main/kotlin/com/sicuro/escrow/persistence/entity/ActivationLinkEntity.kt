package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.LinkType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "activation_link")
@EntityListeners(AuditingEntityListener::class)
data class ActivationLinkEntity constructor(

    @Id
    var uuid: String,

    @Enumerated(EnumType.STRING)
    @Column(name="dtype")
    var type: LinkType,

    var active: Boolean = false,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null

) {
    override fun toString(): String {
        return "ActivationLinkBo{" +
            "uuid='" + uuid +
            ", paymentType='" + type +
            ", active=" + active +
            ", user=" + user +
            ", created="+ created.toString()+
            ", lastModified="+ lastModified.toString()+
            "}"
    }

}
