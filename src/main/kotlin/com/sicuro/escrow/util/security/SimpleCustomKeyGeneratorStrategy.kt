package com.sicuro.escrow.util.security

/**
 * Concrete implementation of CustomKeyGenerator.
 *
 * @since 1.0.0
 * @author engwaambe
 */
open class SimpleCustomKeyGeneratorStrategy : CustomKeyGeneratorStrategy {
    override val customKeyLength: Int
        get() = 11
    override val range: Int
        get() = 9000
    override val upperRangeLimit: Int
        get() = range
    override val lowerRangeLimit: Int
        get() = 0
    override val prexfixHolder: String
        get() = ""
}
