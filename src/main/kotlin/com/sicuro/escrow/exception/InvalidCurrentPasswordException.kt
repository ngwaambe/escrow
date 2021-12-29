package com.sicuro.escrow.exception

class InvalidCurrentPasswordException(override val message: String): ConflictException(message)
