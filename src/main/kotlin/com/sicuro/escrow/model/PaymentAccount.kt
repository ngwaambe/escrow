package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity

abstract class PaymentAccount(
    open val id: Long?,
    open val type: PaymentAccountType,
    open val owner: String
) {
    abstract fun convert(): PaymentAccountEntity

    companion object {
        fun convert(account: PaymentAccountEntity): PaymentAccount {
           return  when(account) {
                is PaypalAcccountEntity -> PaypalAccount.convert(account)
                else -> BankAccount.convert(account)
            }
        }
    }
}
