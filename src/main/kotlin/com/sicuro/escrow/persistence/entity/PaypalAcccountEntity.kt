package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.PaymentAccountType
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "paypal_account")
@DiscriminatorValue("PAYPAL")
data class PaypalAcccountEntity(

    override var id: Long?,

    override var type: PaymentAccountType,

    override var owner: String,

    @Column(name = "paypal_account", nullable = false)
    var paypalAccount: String
) : PaymentAccountEntity(id, type, owner)
