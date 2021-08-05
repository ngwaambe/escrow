package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.CustomerPaymentAccountEntity

data class CustomerPaymentAccount(
    val id:Long?,
    val customerId: Long,
    val defaultAccount: Boolean,
    val paymentAccount: PaymentAccount
) {

    fun convert() : CustomerPaymentAccountEntity {
        return CustomerPaymentAccountEntity(
            id,
            customerId,
            defaultAccount,
            paymentAccount.convert()
        )
    }
    companion object {
        fun convert(entity: CustomerPaymentAccountEntity): CustomerPaymentAccount {
            return CustomerPaymentAccount(
                entity.id,
                entity.customerId,
                entity.defaultAccount,
                PaypalAccount.convert(entity.paymentAccount)
            )
        }
    }
}

