package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.PaymentAccountType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "payment_account")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener::class)
abstract class PaymentAccountEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id:Long?,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    open var type: PaymentAccountType,

    @Column(name = "owner")
    open var owner: String,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    open var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    open var lastModified: OffsetDateTime? = null
)
