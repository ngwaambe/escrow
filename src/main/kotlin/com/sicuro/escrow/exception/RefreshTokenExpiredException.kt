package com.sicuro.escrow.exception

import org.springframework.security.authentication.AccountStatusException

class RefreshTokenExpiredException(override val message: String): AccountStatusException(message)
