package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.AddressEntity
import java.time.OffsetDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class Address(
    val id: Long? = null,
    val street: @NotNull String,
    val houseNumber: String?,
    val streetExtension: String?,
    val postalCode: @NotNull String,
    val city: @NotNull String,
    val region: String?,
    val countryIso: @NotNull @Size(max = 2, min = 2) String,
    val created: @Past OffsetDateTime?,
    val lastModified: @Past OffsetDateTime?
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
        created = OffsetDateTime.now(),
        lastModified = OffsetDateTime.now());

    companion object factory {
        fun convert(address: AddressEntity) = Address(
            address.id,
            address.street,
            address.houseNumber,
            address.streetExtension,
            address.postalCode,
            address.city,
            address.region,
            address.countryIso,
            address.created,
            address.lastModified);
    }
}
