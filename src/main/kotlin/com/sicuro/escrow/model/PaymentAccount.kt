package com.sicuro.escrow.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION,
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes(
    JsonSubTypes.Type(value = BankAccount::class, name = "BankAccount"),
    JsonSubTypes.Type(value = PaypalAccount::class, name = "PaypalAccount")
)
sealed class PaymentAccount(
    open val id: Long?,
    open val paymentType: PaymentAccountType,
    open val owner: String
) {
    abstract fun convert(): PaymentAccountEntity

    companion object {
        fun convert(account: PaymentAccountEntity): PaymentAccount {
            return when (account) {
                is PaypalAcccountEntity -> PaypalAccount.convert(account)
                else -> BankAccount.convert(account)
            }
        }
    }
}
