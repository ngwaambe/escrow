package com.sicuro.escrow.persistence.entity

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "country")
class CountryEntity(

    id:Long?,

    @Column(name = "iso", nullable = false, unique = true)
    var iso: String,

    /**
     * Name of country.cd
     */
    @Column(name = "name", nullable = false, unique = true)
    var name: String,

    /**
     * Countries dailing code.
     */
    @Column(name = "dialing_country_code")
    var dialingCountryCode: String,

    /**
     * International prefix.
     */
    @Column(name = "international_prefix")
    var internationalPrefix: String,

    /**
     * The official currency of this country.
     */
    @Column(name = "currency")
    var currency: String,

    /**
     * A String of the form like ("de" or "en") that represents th JVM language of this country. A country can have more
     * than one language, here we give only one. This term to be changed!
     */
    @Column(name = "language")
    var jvmLanguage: String? = null,

    /**
     * Country belongs to european union.
     */
    @Column(name = "european_union_member", columnDefinition = "int NOT NULL DEFAULT 0")
    var europeanUnionMember: Boolean = false,
    /**
     * Country belongs to SEPA.
     */
    @Column(name = "sepa", columnDefinition = "int NOT NULL DEFAULT 0")
    var sepa: Boolean  = false,

    /**
     * ParticipantPaypalAccount fee to be charged for customer from given country.
     */
    @Column(name = "paypal_fee", columnDefinition = "int(10) NOT NULL DEFAULT 6")
    var paypalFee: Long = 0,

    created: OffsetDateTime,

    lastModified: OffsetDateTime
): BaseEntity(id, created, lastModified) {

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is CountryEntity) return false
        if (!super.equals(obj)) return false

        if (iso != obj.iso) return false
        if (name != obj.name) return false
        if (dialingCountryCode != obj.dialingCountryCode) return false
        if (internationalPrefix != obj.internationalPrefix) return false
        if (currency != obj.currency) return false
        if (jvmLanguage != obj.jvmLanguage) return false
        if (europeanUnionMember != obj.europeanUnionMember) return false
        if (sepa != obj.sepa) return false
        if (paypalFee != obj.paypalFee) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + iso.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + dialingCountryCode.hashCode()
        result = 31 * result + internationalPrefix.hashCode()
        result = 31 * result + currency.hashCode()
        result = 31 * result + (jvmLanguage?.hashCode() ?: 0)
        result = 31 * result + europeanUnionMember.hashCode()
        result = 31 * result + sepa.hashCode()
        result = 31 * result + paypalFee.hashCode()
        return result
    }

    override fun toString(): String {
        return "CountryEntity(iso='$iso', name='$name', dialingCountryCode='$dialingCountryCode', internationalPrefix='$internationalPrefix', currency='$currency', jvmLanguage=$jvmLanguage, europeanUnionMember=$europeanUnionMember, sepa=$sepa, paypalFee=$paypalFee)"
    }


};
