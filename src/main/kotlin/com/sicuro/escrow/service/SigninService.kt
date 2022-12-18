package com.sicuro.escrow.service

import com.sicuro.escrow.exception.RefreshTokenExpiredException
import com.sicuro.escrow.model.CheckTokenRequest
import com.sicuro.escrow.model.RefreshTokenRequest
import com.sicuro.escrow.model.TokenRequest
import com.sicuro.escrow.model.TokenResponse
import com.sicuro.escrow.util.security.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SigninService @Autowired constructor(
    val authenticationManager: AuthenticationManager,
    val jwtUtils: JwtUtils,
    @Value("\${security.jwt.expirationMs}") val jwtExpirationMs: Number,
    @Value("\${security.jwt.refreshExpirationMs}") val refreshJwtExpirationMs: Number
) {

    fun login(tokenRequest: TokenRequest): TokenResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(tokenRequest.username, tokenRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        return createTokenResponse(authentication, jwtExpirationMs, refreshJwtExpirationMs)
    }

    fun refreshSecurityContext(tokenRequest: TokenRequest) {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(tokenRequest.username, tokenRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
    }

    fun refreshToken(refreshRequest: RefreshTokenRequest): TokenResponse {
        if (jwtUtils.validateJwtToken(refreshRequest.token)) {
            return createTokenResponse(SecurityContextHolder.getContext().authentication, jwtExpirationMs, refreshJwtExpirationMs)
        }
        throw RefreshTokenExpiredException("Refresh token has expired ")
    }

    fun checkToken(checkTokenRequest: CheckTokenRequest) = jwtUtils.checkToken(checkTokenRequest.token)

    private fun createTokenResponse(authentication: Authentication, tokenExpiration: Number, refreshTokenExpiration: Number): TokenResponse {
        val token = jwtUtils.generateJWToken(authentication, tokenExpiration)
        val refreshToken = jwtUtils.generateJWToken(authentication, refreshTokenExpiration)
        val exp = jwtUtils.checkToken(token).exp
        return TokenResponse(token, exp, "JWT", refreshToken)
    }

    companion object {
        private var log = LoggerFactory.getLogger(SigninService::class.java)
    }
}
