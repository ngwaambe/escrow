package com.sicuro.escrow.controller

import com.sicuro.escrow.model.*
import com.sicuro.escrow.service.SigninService
import com.sicuro.escrow.service.SignupService
import com.sicuro.escrow.util.security.JwtTokenHelper
import com.sicuro.escrow.util.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@Validated
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    val signinService: SigninService,
    val signupService: SignupService,
    val jwtUtils: JwtUtils
) {

    @PostMapping("/token")
    fun authenticateUser(@RequestBody @Valid request: TokenRequest) = ResponseEntity.ok(signinService.login(request))

    @PostMapping("/check_token")
    fun validateToken(@RequestBody @Valid request: CheckTokenRequest) = ResponseEntity.ok(signinService.checkToken(request))

    @PostMapping("/refresh_token")
    fun refreshToken(@RequestBody @Valid request: RefreshTokenRequest) = ResponseEntity.ok(signinService.refreshToken(request))

    @PostMapping("/signup")
    fun signupUser(@RequestBody @Valid request: SignupRequest) = ResponseEntity.ok(signupService.signup(request))

    @GetMapping("/activate_account/{activationId}")
    fun activateAccount(@PathVariable activationId: String) = ResponseEntity.ok(signupService.activateAccount(activationId))

    @PutMapping("/complete_signup")
    fun completeUserSignup(@RequestBody @Valid request: CompleteSignupRequest, @NotNull @RequestHeader("Authorization") headerAuth: String) {
        JwtTokenHelper.parseJwtToken(headerAuth)?.also {
            val customerId = jwtUtils.getCustomerIdFromToken(it)
            signupService.completeSignup(customerId, request)
        } ?: throw UsernameNotFoundException("Could not resolve user from token")
    }

    @PutMapping("/init_reset_password")
    fun initiatePasswordReset(@RequestBody @Valid request: ResetPasswordRequest) = ResponseEntity.ok(signupService.initiatePasswordReset(request.email))

    @GetMapping("/reset_password/{activationId}")
    fun resetPassword(@PathVariable activationId: String) = ResponseEntity.ok(signupService.resetPassword(activationId))
}
