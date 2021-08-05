package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.BankAccountEntity
import com.sicuro.escrow.persistence.entity.PaymentAccountEntity

data class BankAccount(
    override val id: Long?,
    override val type: PaymentAccountType,
    override val owner: String,
    val bankName: String,
    val iban: String,
    val swiftBic: String,
    val city: String,
    val postalCode: String,
    val countryIso: String
) : PaymentAccount(id, type, owner) {
    override fun convert(): PaymentAccountEntity {
        return BankAccountEntity(
            id,
            type,
            owner,
            bankName,
            iban,
            swiftBic,
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
                account.type,
                account.owner,
                (account as BankAccountEntity).bankName,
                account.iban,
                account.swiftBic,
                account.city,
                account.postalCode,
                account.countryIso
            )
        }
    }
}
