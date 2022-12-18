package com.sicuro.escrow.model

import javax.validation.Valid

data class SignupRequest(

    @get:Valid
    val organisation: Organisation?,

    @get:Valid
    val contact: Contact,
)

data class CompleteSignupRequest(
    @Valid
    val address: Address,
    @Valid
    val securityQuestion: SecurityQuestionDto
)

data class SecurityQuestionResponse(
    val question: SecurityQuestion,
    val activationId: String
)

data class ResetPasswordRequest(
    val questionAnswer: String,
    val activationId: String
)

data class InitiatResetPasswordRequest(
    val email: String
)
