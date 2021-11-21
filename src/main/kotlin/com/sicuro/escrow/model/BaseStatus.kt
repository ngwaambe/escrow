package com.sicuro.escrow.model

/**
 * Decribes various states an instance of a business Object can have.
 *
 * @since 1.0.0
 * @author engwaambe
 */
enum class BaseStatus() {
    /**
     * Business object is active. Has taken part in a business activity within a period defined by system adminstrator
     * or active because certain rules or conditions have been fullfilled.
     */
    active,

    /**
     * Status user entity has when password change is pending
     */
    temporary_password,

    /**
     * Business object is not active. Has not taken part in a business activity within a period defined by system
     * adminstrator or not active because certain rules or conditions have not been fullfilled.
     */
    inactive,

    /**
     * Business object or entity has been deactivated. Can not take part in any business activity.
     */
    deactivated,

    /**
     * Business object or entity has been fictively deleted from system. Is considered deleted but is still available in
     * the system storage system.
     */
    fictive_deleted;

}
