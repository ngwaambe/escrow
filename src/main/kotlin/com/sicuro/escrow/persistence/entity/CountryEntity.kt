package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "country")
@EntityListeners(AuditingEntityListener::class)
data class CountryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,

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

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
){

    override fun toString(): String {
        return "CountryEntity(" +
            "iso='$iso', " +
            "name='$name', " +
            "dialingCountryCode='$dialingCountryCode', " +
            "internationalPrefix='$internationalPrefix', " +
            "currency='$currency', " +
            "jvmLanguage=$jvmLanguage, " +
            "europeanUnionMember=$europeanUnionMember, " +
            "sepa=$sepa, " +
            "paypalFee=$paypalFee)"
    }


}
