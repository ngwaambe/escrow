package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.CustomerEntity

data class Customer constructor(
    val id: Long? = null,

    val customerNumber: String,

    val title: Title,

    val firstName: String,

    val lastName: String,

    val gender: Gender,

    val email: String,

    val language: String,

    val address: Address? = null,

    val organisation: String? = null,

    val taxNumber: String? = null,

    val applyVat: Boolean = false,

    val partnerId: String? = null,

    val identityNumber: String?
) {

    constructor(customerNumber: String, title: Title, firstName: String, lastName: String, preferedLanguage: String, email: String, organisation: String?, taxNumber: String?) :
        this(
            null,
            customerNumber,
            title,
            firstName,
            lastName,
            title.gender,
            email,
            preferedLanguage,
            null,
            organisation,
            taxNumber,
            false,
            null,
            null,
        )

    fun convert(): CustomerEntity {
        return CustomerEntity(
            id,
            customerNumber,
            title,
            firstName,
            lastName,
            gender,
            email,
            language,
            applyVat,
            organisation,
            address?.convert(),
            taxNumber,
            partnerId,
            identityNumber
        )
    }

    companion object {
        fun convert(obj: CustomerEntity): Customer {
            val address = if (obj.address !== null) {
                Address.convert(obj.address!!)
            } else {
                null
            }
            return Customer(
                obj.id,
                obj.customerNumber,
                obj.title,
                obj.firstName,
                obj.lastName,
                obj.gender,
                obj.email,
                obj.language,
                address,
                obj.organisation,
                obj.taxNumber,
                obj.applyVat,
                obj.partnerId,
                obj.identityNumber
            )
        }
    }

}
