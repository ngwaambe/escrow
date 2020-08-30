package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.CustomerEntity
import java.time.OffsetDateTime

data class Customer constructor(
    val id: Long? = null,

    val customerNumber: String,

    val partnerId: String? = null,

    val identityNumber: String?,

    val title: Title,

    val firstName: String,

    val lastName: String,

    val married: Boolean = false,

    val gender: Gender,

    val organisation: String? = null,

    val preferedLanguage: String? = null,

    val allowNewsLetterNotification: Boolean = false,

    val applyVat: Boolean = false,

    val address: Address? = null,

    val taxNumber: String? = null,

    val phoneNumber: String? = null,

    val fax: String? = null,

    val email: String,

    val website: String? = null,

    val created: OffsetDateTime?=null,

    val lastModified: OffsetDateTime?=null) {

    constructor(customerNumber: String,title: Title,firstName: String,lastName: String, preferedLanguage: String, email:String, organisation: String?, taxNumber: String?):
        this(null, customerNumber, null, null, title, firstName, lastName, false, title.gender, organisation, preferedLanguage, false, false, null, taxNumber, null, null, email, null, null, null);

    fun convert(): CustomerEntity {
        return CustomerEntity(
            id,
            customerNumber,
            partnerId,
            identityNumber,
            title,
            firstName,
            lastName,
            married,
            gender,
            organisation,
            preferedLanguage,
            allowNewsLetterNotification,
            applyVat,
            address?.convert(),
            taxNumber,
            phoneNumber,
            fax,
            email,
            website,
            created?: OffsetDateTime.now(),
            lastModified?: OffsetDateTime.now());
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
                obj.partnerId,
                obj.identityNumber,
                obj.title,
                obj.firstName,
                obj.lastName,
                obj.married,
                obj.gender,
                obj.organisation,
                obj.preferedLanguage,
                obj.allowNewsLetterNotification,
                obj.applyVat,
                address,
                obj.taxNumber,
                obj.phoneNumber,
                obj.fax,
                obj.email,
                obj.website,
                obj.created,
                obj.lastModified);
        }
    }

}
