package com.sicuro.escrow.model

import javax.validation.Valid
import javax.validation.constraints.NotNull

data class SignupRequest(

    @get:Valid
    val organisation: Organisation?,

    @get:Valid
    val contact: Contact,
)

data class CompleteSignupRequest (
    @Valid
    val address: Address,
    @Valid
    val securityQuestion: SecurityQuestionDto
)

data class ResetPasswordRequest (
    val email:String
)


