package com.sicuro.escrow.model

import com.fasterxml.jackson.annotation.JsonTypeName
import com.sicuro.escrow.persistence.entity.BankAccountEntity
import com.sicuro.escrow.persistence.entity.PaymentAccountEntity

@JsonTypeName("BankAccount")
data class BankAccount(
    override val id: Long?,
    override val owner: String,
    val bankName: String,
    val iban: String,
    val swiftCode: String,
    val city: String,
    val postalCode: String,
    val countryIso: String
) : PaymentAccount(id, PaymentAccountType.BANK, owner) {
    override fun convert(): PaymentAccountEntity {
        return BankAccountEntity(
            id,
            paymentType,
            owner,
            bankName,
            iban,
            swiftCode,
            city,
            postalCode,
            countryIso
        )
    }

    companion object {
        fun convert(account: PaymentAccountEntity): PaymentAccount {
            require(account.type == PaymentAccountType.BANK) {
                "Must be a bank account"
            }
            return BankAccount(
                account.id,
                account.owner,
                (account as BankAccountEntity).bankName,
                account.iban,
                account.swiftCode,
                account.city,
                account.postalCode,
                account.countryIso
            )
        }
    }
}
