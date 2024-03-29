package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.AddressEntity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class
Address(
    val id: Long? = null,
    val street: @NotNull String,
    val houseNumber: String?,
    val streetExtension: String?,
    val postalCode: @NotNull String,
    val city: @NotNull String,
    val region: String?,
    val countryIso: @NotNull @Size(max = 2, min = 2) String,
    val phoneNumber: String
) {
    fun convert() = AddressEntity(
        id,
        street,
        streetExtension,
        houseNumber,
        postalCode,
        region,
        city,
        countryIso,
        phoneNumber);

    companion object Factory {
        fun convert(address: AddressEntity) = Address(
            address.id,
            address.street,
            address.houseNumber,
            address.streetExtension,
            address.postalCode,
            address.city,
            address.region,
            address.countryIso,
            address.phoneNumber);
    }
}
