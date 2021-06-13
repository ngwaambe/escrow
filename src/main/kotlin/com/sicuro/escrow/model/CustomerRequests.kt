package com.sicuro.escrow.model

import javax.validation.Valid
import javax.validation.constraints.*
import kotlin.math.min

data class Organisation(
    @get:NotEmpty
    val name:String,

    @get:NotEmpty
    val taxNumber:String
)

data class Contact(
    @get:NotNull
    val title:Title,

    @get:NotEmpty
    val language: String,

    @get:NotEmpty
    val firstname:String,

    @get:NotEmpty
    val lastname:String,

    @get:Email
    @get:NotEmpty
    val email:String,

    @get:Size(min=8)
    //@get:Pattern(regexp = "^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})", message = "Not all password criterias were satisfied")
    @get:NotEmpty
    val password:String
)

data class SecurityQuestionDto(
    @get:NotEmpty
    val question: SecurityQuestion,

    @get:NotEmpty
    val answer: String
)

data class CustomerDetailRequest(

    @get:NotNull
    val title:Title,

    @get:NotEmpty
    val language: String,

    @get:NotEmpty
    val firstname:String,

    @get:NotEmpty
    val lastname:String,

    @get:Valid
    val organisation: Organisation? = null
)

data class ChangePasswordRequest(
    @get:NotEmpty
    @get:Size(min=8)
    val password:String
)

data class ChangeEmailRequest(
    val email: String
)

data class CustomerCreateRequest(

    @get:Valid
    val contact: Contact,

    @get:Valid
    val address: Address,

    @get:NotNull
    val roles: List<String>,

    @get:Valid
    val organisation: Organisation?,

    val partnerId: String?,

    val identityNumber: String?

)

data class CustomerFilterRequest(
    @get:NotNull
    val offset: Int,

    @get:NotNull
    val limit: Int,

    val sortOrder: SortOrder?,

    val sortField: String?,

    val customerNr: String?,

    val email: String?,

    val firstname: String?,

    val lastname: String?,

    val language: String?,

    val country: String?,

    val city:String?,

    val status: BaseStatus?
)
