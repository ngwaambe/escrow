package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "language")
@EntityListeners(AuditingEntityListener::class)
data class LanguageEntity constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,

    @Column(nullable = false, length = 80, unique = true, name = "name")
    var name: String,

    @Column(nullable = false)
    var iso: String? = null,

    @Column(nullable = false, name="system_supported_language")
    var systemSupported: Boolean,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
)
