package com.sicuro.escrow.model

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

data class TokenRequest(

    @get:NotBlank
    val username:String,

    @get:NotBlank
    val password:String
)

data class TokenResponse @JvmOverloads constructor(
    val access_token: String,
    val expires_in: Long,
    val type: String? = "JWT",
    val refresh_token: String? = null,
)

data class CheckTokenRequest (
    @get:NotBlank
    val token: String
)

data class RefreshTokenRequest (
    @get:NotBlank
    val token: String
 )

data class CheckTokenResponse (
    val active: Boolean,
    val exp: Long,
    val error: String?
)
