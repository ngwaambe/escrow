package com.sicuro.escrow.util.security

import java.time.OffsetDateTime

/**
 * Concrete implementation of CustomKeyGeneratorFactory.
 *
 * @since 1.0.0
 * @author engwaambe
 */
class CustomKeyGeneratorFactoryService internal constructor() : CustomKeyGeneratorFactory {
    override fun createCustomKeyGenerator(strategy: CustomKeyGeneratorStrategy, seed: OffsetDateTime): CustomKeyGenerator {
        return SimpleCutomKeyGenerator(seed, strategy)
    }

    override fun createDefault(seed: OffsetDateTime): CustomKeyGenerator {
        return SimpleCutomKeyGenerator(seed, SimpleCustomKeyGeneratorStrategy())
    }

    companion object {
        @JvmStatic
        val instance: CustomKeyGeneratorFactory
            get() = CustomKeyGeneratorFactoryService()
    }
}
