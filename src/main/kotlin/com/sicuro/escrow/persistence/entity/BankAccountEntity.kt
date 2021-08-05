package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.PaymentAccountType
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "bank_account")
@DiscriminatorValue("BANK")
data class BankAccountEntity(
    override var id:Long?,

    override var type: PaymentAccountType,

    override var owner: String,

    @Column(name = "bank_name")
    var bankName: String,

    @Column(name = "iban")
    var iban: String,

    @Column(name = "swiftBic")
    var swiftBic: String,

    @Column(name = "city")
    var city: String,

    @Column(name = "postal_code")
    var postalCode: String,

    @Column(name = "country_iso")
    var countryIso: String
): PaymentAccountEntity(id, type, owner)
