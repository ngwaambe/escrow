package com.sicuro.escrow.model

enum class Gender(
    var value: String,
    var i18n: String) {
    MALE("Male", "male"),
    FEMALE("Female", "female");

    companion object {
        fun getByName(name: String): Gender {

            if (MALE.name.equals(name, ignoreCase = true)) {
                return MALE
            }
            if (FEMALE.name.equals(name, ignoreCase = true)) {
                return FEMALE
            }
            val pattern = "There is no gender instance for name %s"
            throw IllegalArgumentException(String.format(pattern, name))
        }
    }
}
