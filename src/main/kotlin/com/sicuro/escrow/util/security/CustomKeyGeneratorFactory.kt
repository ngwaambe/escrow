package com.sicuro.escrow.util.security

import java.time.OffsetDateTime

/**
 * Factory to access CustomKey generators.
 *
 * @since 1.0.0
 * @author engwaambe
 */
interface CustomKeyGeneratorFactory {

    fun createDefault(seed: OffsetDateTime): CustomKeyGenerator

    fun createCustomKeyGenerator(strategy: CustomKeyGeneratorStrategy, seed: OffsetDateTime): CustomKeyGenerator
}
