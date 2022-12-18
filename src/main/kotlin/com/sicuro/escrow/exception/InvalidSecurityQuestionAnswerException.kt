package com.sicuro.escrow.exception

class InvalidSecurityQuestionAnswerException(override val message: String): ConflictException(message)
