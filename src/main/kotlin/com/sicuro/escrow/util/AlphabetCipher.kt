package com.sicuro.escrow.util

import java.util.*

/**
 * Helper class. Maps Alphabet to a its integer representation in String form.
 *
 * @since 1.0.0
 * @author engwaambe
 */
object AlphabetCipher {
    /**
     * Internal registry for Alphabet and their integer representation.
     */
    private val map = HashMap<String, String>()

    fun getAlphabet(number: Int): String {
        return map[String.format("%02d", number)]!!
    }

    fun getAlphabet(number: String): String {
        return map[number]!!
    }

    init {
        map[String.format("%02d", 1)] = "A"
        map[String.format("%02d", 2)] = "B"
        map[String.format("%02d", 3)] = "C"
        map[String.format("%02d", 4)] = "D"
        map[String.format("%02d", 5)] = "E"
        map[String.format("%02d", 6)] = "F"
        map[String.format("%02d", 7)] = "G"
        map[String.format("%02d", 8)] = "H"
        map[String.format("%02d", 9)] = "I"
        map[String.format("%02d", 10)] = "J"
        map[String.format("%02d", 11)] = "K"
        map[String.format("%02d", 12)] = "L"
        map[String.format("%02d", 13)] = "M"
        map[String.format("%02d", 14)] = "N"
        map[String.format("%02d", 15)] = "O"
        map[String.format("%02d", 16)] = "P"
        map[String.format("%02d", 17)] = "Q"
        map[String.format("%02d", 18)] = "R"
        map[String.format("%02d", 19)] = "S"
        map[String.format("%02d", 20)] = "T"
        map[String.format("%02d", 21)] = "U"
        map[String.format("%02d", 22)] = "V"
        map[String.format("%02d", 23)] = "W"
        map[String.format("%02d", 24)] = "X"
        map[String.format("%02d", 25)] = "Y"
        map[String.format("%02d", 26)] = "Z"
        map[String.format("%02d", 27)] = "ZA"
        map[String.format("%02d", 28)] = "YB"
        map[String.format("%02d", 29)] = "XC"
        map[String.format("%02d", 30)] = "WD"
        map[String.format("%02d", 31)] = "VE"
    }
}
