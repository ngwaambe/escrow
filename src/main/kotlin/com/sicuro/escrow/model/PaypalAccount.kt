package com.sicuro.escrow.model

import com.fasterxml.jackson.annotation.JsonTypeName
import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity

@JsonTypeName("PaypalAccount")
data class PaypalAccount(
    override val id:Long?,
    override val owner:String,
    val paypalAccount: String
): PaymentAccount(id, PaymentAccountType.PAYPAL, owner) {
    override fun convert(): PaymentAccountEntity {
        return PaypalAcccountEntity(
            id,
            paymentType,
            owner,
            paypalAccount
        )
    }

    companion object {
        fun convert(account: PaymentAccountEntity): PaymentAccount {
            require(account.type == PaymentAccountType.PAYPAL){
                "Must be a paypal account"
            }
            return PaypalAccount(
                account.id,
                account.owner,
                (account as PaypalAcccountEntity).paypalAccount
            )
        }
    }
}
