package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
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
class AddressEntity constructor(

    id:Long?,
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

    created: OffsetDateTime,

    lastModified: OffsetDateTime


): BaseEntity(id, created, lastModified) {
    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is AddressEntity) return false
        if (!super.equals(obj)) return false

        if (street != obj.street) return false
        if (streetExtension != obj.streetExtension) return false
        if (houseNumber != obj.houseNumber) return false
        if (postalCode != obj.postalCode) return false
        if (region != obj.region) return false
        if (city != obj.city) return false
        if (countryIso != obj.countryIso) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + (streetExtension?.hashCode() ?: 0)
        result = 31 * result + (houseNumber?.hashCode() ?: 0)
        result = 31 * result + postalCode.hashCode()
        result = 31 * result + (region?.hashCode() ?: 0)
        result = 31 * result + city.hashCode()
        result = 31 * result + countryIso.hashCode()
        return result
    }
}
