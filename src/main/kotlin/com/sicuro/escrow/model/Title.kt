package com.sicuro.escrow.model

import com.sicuro.escrow.model.Gender

/**
 * Enumenation of Customer Title
 *
 * @since 1.0.0
 * @author engwaambe
 */
enum class Title( var value: String, var elKey: String, var gender: Gender) {
    Mr("Mr", "title.Mr", Gender.MALE),
    Ms("Ms", "title.Ms", Gender.FEMALE),
    Mr_Dr("Mr Dr", "title.Mr_Dr", Gender.MALE),
    Mrs_Dr("Ms Dr", "title.Ms_Dr", Gender.FEMALE),
    Mr_Prof("Mr Prof", "title.Mr_Prof", Gender.MALE),
    Mrs_Prof("Ms Prof", "title.Ms_Prof", Gender.FEMALE);

    companion object {
        fun parseString(title: String?): Title? {
            for (item in values()) {
                if (item.toString().equals(title, ignoreCase = true)) {
                    return item
                }
            }
            return null
        }
    }
}
