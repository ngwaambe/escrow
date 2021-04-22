package com.sicuro.escrow.util.security

import com.sicuro.escrow.util.AlphabetCipher
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

/**
 * Concrete implementation of CustomKeyGenerator. This class makes use of a given(best birthDate) to
 * generate a unique key.
 *
 * @author engwaambe
 */
open class SimpleCutomKeyGenerator : CustomKeyGenerator {
    private var date: OffsetDateTime
    var strategy: CustomKeyGeneratorStrategy
    var random: Random? = null

    constructor(date: OffsetDateTime, strategy: CustomKeyGeneratorStrategy) {
        this.date = date
        this.strategy = strategy
        init()
    }

    constructor() {
        date = OffsetDateTime.now()
        strategy = SimpleCustomKeyGeneratorStrategy()
    }

    /**
     * Initializes the Random instance as set a seed based on the date value.
     */
    private fun init() {
        val dateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter()

        random = Random()
        val seed = date.format(dateTimeFormatter).toLong()
        random!!.setSeed(seed)
    }

    override fun generateCustomKey(): String {
        val prefix = getPrefix(date)
        val length = prefix.length
        val remaining = strategy.customKeyLength!! - length
        if (remaining > 0) {
            val suffix = String.format("%0" + remaining + "d", randomSuffix)
            return String.format("%s%s", prefix, suffix)
        }
        return prefix.substring(0, strategy.customKeyLength!!)
    }

    protected val randomSuffix: Int
        protected get() {
            val max = strategy.upperRangeLimit
            val min = strategy.lowerRangeLimit
            return if (max != null && min != null) {
                random!!.nextInt(max - min + 1) + min
            } else {
                random!!.nextInt(strategy.range!!)
            }
        }

    /**
     * Converts date in format yyyy[mm][dd] where by [mm] and [dd] are replaced by their equivalent
     * value in the roman alphabet
     *
     * @param birthDate
     * @return
     */
    protected fun getPrefix(birthDate: OffsetDateTime): String {
        var dateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4).toFormatter()
        val year = birthDate.format(dateTimeFormatter).toLong()

        dateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.MONTH_OF_YEAR, 2).toFormatter()
        val month: String = AlphabetCipher.getAlphabet(birthDate.format(dateTimeFormatter).toInt())

        dateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter()
        val day: String = AlphabetCipher.getAlphabet(birthDate.format(dateTimeFormatter).toInt())

        return String.format("%s%s%s%s", strategy.prexfixHolder, year, month, day)
    }

    fun getDate(): OffsetDateTime? {
        return date
    }

    fun setDate(date: OffsetDateTime) {
        this.date = date
    }
}
