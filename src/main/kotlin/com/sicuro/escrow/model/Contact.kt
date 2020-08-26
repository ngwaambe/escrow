package com.sicuro.escrow.model

import com.sicuro.escrow.persistence.entity.ContactEntity
import java.time.OffsetDateTime

data class Contact(
    var id: Long? = null,

    var customerNumber: String? = null,

    var partnerId: String? = null,

    var identityNumber: String,

    var title: Title,

    var firstName: String,

    var lastName: String,

    var married: Boolean = false,

    var gender: Gender,

    var organisation: String? = null,

    var preferedLanguage: String? = null,

    var allowNewsLetterNotification: Boolean = false,

    var applyVat: Boolean = false,

    var address: Address? = null,

    var taxNumber: String? = null,

    var phoneNumber: String? = null,

    var fax: String? = null,

    var email: String? = null,

    var website: String? = null,

    var created: OffsetDateTime?=null,

    var lastModified: OffsetDateTime?=null) {

    fun convert(): ContactEntity {
        return ContactEntity(
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
        fun convert(obj: ContactEntity): Contact {
            val address = if (obj.address !== null) {
                Address.convert(obj.address!!)
            } else {
                null
            }
            return Contact(
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
