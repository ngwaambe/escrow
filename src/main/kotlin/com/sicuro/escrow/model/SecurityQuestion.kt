package com.sicuro.escrow.model

/**
 * Describes various security question which is used to protect user personal data.
 *
 * @since 1.0.0
 * @author engwaambe
 */
enum class SecurityQuestion(var question: String, var messageKey: String) {
    GRAND_MOTHERS_MAIDEN_NAME("What is your maternal grandmother's maiden name", "securityQuestion.grandMothersMaidenName"),
    CHILD_HOOD_NICNAME("What was your childhood nickname", "securityQuestion.childHoodNickName"),
    PLACE_FIRST_KISS("Where were you when you had your first kiss", "securityQuestion.placeFirstKiss"),
    PLACE_MOTHER_FATHER_MEET("In what city or town did your mother and father meet", "securityQuestion.placeMotherFatehrMeet"),
    DAD_LICENCE_PLATE_FIRST_CAR("What is the license plate (registration) of your dad's first car", "securityQuestion.dadLicencePlateFirstCar")
}
