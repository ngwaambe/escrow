package com.sicuro.escrow.persistence.entity

import com.sicuro.escrow.model.Gender
import com.sicuro.escrow.model.Title
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * Class defines properties general to all contacts in the system
 *
 * @since 1.0.0
 * @author engwaambe
 */
@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener::class)
data class CustomerEntity @JvmOverloads constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,

    @Column(name = "id_number", nullable = false, unique = true)
    var customerNumber: String,

    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    var title: Title,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    var gender: Gender,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "prefered_language_iso", nullable = false)
    var language: String,

    @Column(name = "apply_vat", nullable = false)
    var applyVat: Boolean = false,

    @Column(name = "organisation")
    var organisation: String? = null,

    @Fetch(FetchMode.JOIN)
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    var address: AddressEntity? = null,

    @Column(name = "tax_number")
    var taxNumber: String? = null,

    @Column(name = "partner_id")
    var partnerId: String? = null,

    @Column(name = "id_card_number")
    var identityNumber: String? = null,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
)
