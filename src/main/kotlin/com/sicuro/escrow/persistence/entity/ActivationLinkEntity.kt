package com.sicuro.escrow.persistence.entity

import javax.persistence.*

/**
 * Activation link check.
 *
 * @author engwaambe
 */
@Entity
@Table(name = "activation_link")
class ActivationLinkEntity constructor(
    /**
     * unique random generated id.
     */
    var uuid: String? = null,

    /**
     * states if link is still active;
     */
    var isActive: Boolean = false,

    /**
     * Customer ID
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    var customer: CustomerEntity? = null

): BaseEntity () {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ActivationLinkEntity) return false
        if (!super.equals(o)) return false
        val that = o
        if (isActive != that.isActive) return false
        return if (if (uuid != null) uuid != that.uuid else that.uuid != null) false else !if (customer != null) !customer!!.equals(that.customer) else that.customer != null
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + if (uuid != null) uuid.hashCode() else 0
        result = 31 * result + if (isActive) 1 else 0
        result = 31 * result + if (customer != null) customer.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "ActivationLinkBo{" +
            "uuid='" + uuid + '\'' +
            ", active=" + isActive +
            ", customer=" + customer +
            '}'
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = -2551113812964071314L
    }
}
