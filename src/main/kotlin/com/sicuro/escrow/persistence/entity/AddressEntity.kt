package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * Defines geographic address.
 *
 * @since 1.0.0
 * @author Malkhaz
 * @auther ngwaambe
 */
@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener::class)
data class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,
    /**
     * Street.
     */
    @Column(name = "street", nullable = false)
    var street: String,

    /**
     * Street extension.
     */
    @Column(name = "street_extension")
    var streetExtension: String?,

    /**
     * House number.
     */
    @Column(name = "house_number")
    var houseNumber: String?,

    /**
     * Postal code.
     */
    @Column(name = "postal_code", nullable = false)
    var postalCode: String,

    /**
     * Regions name.
     */
    @Column(name = "region")
    var region: String?,

    /**
     * City name.
     */
    @Column(name = "city", nullable = false)
    var city: String,

    @Column(name = "country_iso", nullable = false)
    var countryIso: String,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
)
