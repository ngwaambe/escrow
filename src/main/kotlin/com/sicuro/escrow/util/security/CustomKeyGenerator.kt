package com.sicuro.escrow.util.security

/**
 * Defines interface for custom key generator.
 *
 * @since 1.0.0
 * @author engwaambe
 */
interface CustomKeyGenerator {
    /**
     * Generates a system wide unique key based on the given strategy
     *
     * @return String (never null)
     */
    fun generateCustomKey(): String
}
