package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "customer_payment_account")
@EntityListeners(AuditingEntityListener::class)
data class CustomerPaymentAccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "customer_id")
    val customerId: Long,

    @Column(name = "default_account")
    val defaultAccount: Boolean,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_account_id")
    val paymentAccount: PaymentAccountEntity,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
)
