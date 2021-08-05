package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity

data class PaypalAccount(
    override val id:Long?,
    override val type: PaymentAccountType,
    override val owner:String,
    val paypalAccount: String
): PaymentAccount(id, type, owner) {
    override fun convert(): PaymentAccountEntity {
        return PaypalAcccountEntity(
            id,
            type,
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
                account.type,
                account.owner,
                (account as PaypalAcccountEntity).paypalAccount
            )
        }
    }
}
