package com.sicuro.escrow.util.security

/**
 * Define Criterias which a custom key muss fullfill.
 *
 * @since 1.0.0
 * @author engwaambe
 */
interface CustomKeyGeneratorStrategy {
    /**
     * Constant prefix to set at the beginning of customkey.
     *
     * @return
     */
    val prexfixHolder: String?

    /**
     * Length of the custom key.
     *
     * @return
     */
    val customKeyLength: Int?

    /**
     * Get numeric range for the custom key
     *
     * @return
     */
    val range: Int?

    /**
     * Get upper Range limit
     *
     * @return
     */
    val upperRangeLimit: Int?

    /**
     * Get Lower Range limit.
     *
     * @return
     */
    val lowerRangeLimit: Int?
}
