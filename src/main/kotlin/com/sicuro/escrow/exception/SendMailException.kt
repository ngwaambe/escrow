package com.sicuro.escrow.exception

class SendMailException(override val message: String, override val cause: Throwable) : RuntimeException(message, cause) {
}
