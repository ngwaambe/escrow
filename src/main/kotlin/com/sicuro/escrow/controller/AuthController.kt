package com.sicuro.escrow.controller

import com.sicuro.escrow.model.TokenRequest
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.model.CheckTokenRequest
import com.sicuro.escrow.model.RefreshTokenRequest
import com.sicuro.escrow.service.SigninService
import com.sicuro.escrow.service.SignupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    val signinService: SigninService,
    val signupService: SignupService) {

    @PostMapping("/token")
    fun authenticateUse(@Valid @RequestBody request: TokenRequest) = ResponseEntity.ok(signinService.login(request))

    @PostMapping("/check_token")
    fun validateToken(@Valid @RequestBody request: CheckTokenRequest) = ResponseEntity.ok(signinService.checkToken(request))

    @PostMapping("/refresh_token")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest) = ResponseEntity.ok(signinService.refreshToken(request))

    @PostMapping("/signup")
    fun signupUser(@Valid @RequestBody request: SignupRequest) = ResponseEntity.ok(signupService.signup(request))

    @GetMapping("/activate/{activationId}")
    fun activateAccount(@PathVariable activationId: String) = ResponseEntity.ok(signupService.activateAccount(activationId))

}
