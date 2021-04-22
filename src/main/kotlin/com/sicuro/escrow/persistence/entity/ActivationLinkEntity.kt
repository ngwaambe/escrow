package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "activation_link")
@EntityListeners(AuditingEntityListener::class)
class ActivationLinkEntity constructor(

    @Id
    var uuid: String,

    /**
     * states if link is still active;
     */
    var active: Boolean = false,

    /**
     * Customer ID
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    var customer: CustomerEntity,

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
            ", active=" + active +
            ", customer=" + customer +
            ", created="+ created.toString()+
            ", lastModified="+ lastModified.toString()+
            "}"
    }

}
