package com.sicuro.escrow.persistence.entity

import org.springframework.data.annotation.AccessType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * Generall entity class.
 *
 * @since 1.0.0
 * @author engwaambe
 */
@MappedSuperclass
open class BaseEntity constructor(
    @Id
    @AccessType(value = AccessType.Type.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Long? = null,

    @CreatedDate
    @Column(name = "created", nullable = false, insertable = true, updatable = false)
    var created: OffsetDateTime? = null,

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false, insertable = true, updatable = true)
    var lastModified: OffsetDateTime? = null
) : Serializable {


    override fun toString(): String {
        return String.format("%s[id=%d]", super.toString(), id)
    }

    /**
     * Overrides the [default equals()][Object.equals] to check equality using the type (through
     * [Object.getClass]) only. Subclasses should invoke this method first in their `equals()`, and
     * continue only if equality has been reported as `true`. This way an object hierarchy can be maintained with
     * both the ability for polymorphism in JPA as well as obeying the general `equals()` contract.
     *
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as BaseEntity
        return if (id == null) other.id == null else id == other.id
    }

    /**
     * Overrides the [default hashCode()][Object.hashCode] to return always null. This is supposed to aid in
     * debugging across different session, which will result in the same hash code every time if subclasses follow the
     * same paradigm.
     *
     * @return always 0
     */
    override fun hashCode(): Int {
        return 0
    }

    companion object {
        /**
         * The default serial version ID.
         */
        private const val serialVersionUID = 8754912781258229973L

    }
}
