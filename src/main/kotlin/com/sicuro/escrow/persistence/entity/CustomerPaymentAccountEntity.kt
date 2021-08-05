package com.sicuro.escrow.persistence.entity

import javax.persistence.*

@Entity
@Table(name="contact_payment_account")
data class CustomerPaymentAccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long?,

    @Column(name = "customer_id")
    val customerId: Long,

    @Column(name = "default_account")
    val defaultAccount: Boolean,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_account_id")
    val paymentAccount: PaymentAccountEntity
)
