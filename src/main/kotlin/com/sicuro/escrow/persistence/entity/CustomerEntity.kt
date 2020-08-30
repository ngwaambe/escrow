package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.Gender
import com.sicuro.escrow.model.Title
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * Class defines properties general to all contacts in the system
 *
 * @since 1.0.0
 * @author engwaambe
 */
@Entity
@Table(name = "contacts")
class CustomerEntity(
    id: Long? = null,

    @Column(name = "id_number", nullable = false, unique = true)
    var customerNumber: String,


    @Column(name = "partner_id")
    var partnerId: String? = null,


    @Column(name = "id_card_number")
    var identityNumber: String? = null,


    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    var title: Title,

    @Column(name = "first_name", nullable = false)
    var firstName: String,


    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "married")
    var married: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    var gender: Gender,

    @Column(name = "organisation")
    var organisation: String? = null,

    @Column(name = "prefered_language_iso", nullable = false)
    var preferedLanguage: String? = null,

    @Column(name = "allow_newsletter_notification", nullable = false)
    var allowNewsLetterNotification: Boolean = false,


    @Column(name = "apply_vat", nullable = false)
    var applyVat: Boolean = false,

    @Fetch(FetchMode.JOIN)
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    var address: AddressEntity? = null,

    @Column(name = "tax_number")
    var taxNumber: String? = null,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "fax")
    var fax: String? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "website")
    var website: String? = null,

    created: OffsetDateTime? = null,

    lastModified: OffsetDateTime? = null) : BaseEntity(id, created, lastModified) {

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is CustomerEntity) return false
        if (!super.equals(obj)) return false

        if (customerNumber != obj.customerNumber) return false
        if (partnerId != obj.partnerId) return false
        if (identityNumber != obj.identityNumber) return false
        if (title != obj.title) return false
        if (firstName != obj.firstName) return false
        if (lastName != obj.lastName) return false
        if (married != obj.married) return false
        if (gender != obj.gender) return false
        if (organisation != obj.organisation) return false
        if (preferedLanguage != obj.preferedLanguage) return false
        if (allowNewsLetterNotification != obj.allowNewsLetterNotification) return false
        if (applyVat != obj.applyVat) return false
        if (address != obj.address) return false
        if (taxNumber != obj.taxNumber) return false
        if (phoneNumber != obj.phoneNumber) return false
        if (fax != obj.fax) return false
        if (email != obj.email) return false
        if (website != obj.website) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (customerNumber?.hashCode() ?: 0)
        result = 31 * result + (partnerId?.hashCode() ?: 0)
        result = 31 * result + identityNumber.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + married.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (organisation?.hashCode() ?: 0)
        result = 31 * result + (preferedLanguage?.hashCode() ?: 0)
        result = 31 * result + allowNewsLetterNotification.hashCode()
        result = 31 * result + applyVat.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + (taxNumber?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        result = 31 * result + (fax?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (website?.hashCode() ?: 0)
        return result
    }
}
