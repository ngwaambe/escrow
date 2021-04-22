package com.sicuro.escrow.model

import javax.validation.constraints.NotBlank

data class TokenRequest(

    @NotBlank
    val username:String,

    @NotBlank
    val password:String
)

data class TokenResponse @JvmOverloads constructor(
    val access_token: String,
    val expires_in: Long,
    val type: String? = "JWT",
    val refresh_token: String? = null,
)

data class CheckTokenRequest (
    @NotBlank
    val token: String
)

data class RefreshTokenRequest (
    @NotBlank
    val token: String
 )

data class CheckTokenResponse (
    val active: Boolean,
    val exp: Long,
    val error: String?
)
