package com.sicuro.escrow.controller

import com.sicuro.escrow.exception.ObjectAlreadyExistException
import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.UserAlreadyExistException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(
    val title: String,
    val status: String,
    val message: String? = null,
    val validationErrors: MutableList<ValidationError>? = null,
    val code: String? = null
)

data class ValidationError(
    val name: String,
    val reason: String?,
    val message: String
)

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun objectNotExistHandler(e: ObjectNotFoundException) = ResponseEntity(
        ErrorResponse("Entity not found", HttpStatus.NOT_FOUND.value().toString(), e.message),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(*[UsernameNotFoundException::class, BadCredentialsException::class])
    fun UsernameNotFoundExceptionHandler(e: AuthenticationException) = ResponseEntity(
        ErrorResponse("Entity not found", HttpStatus.NOT_FOUND.value().toString(), e.message),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(ObjectAlreadyExistException::class)
    fun objectAlreadyExistHandler(e: ObjectAlreadyExistException) = ResponseEntity(
        ErrorResponse("Entity already exist", HttpStatus.CONFLICT.value().toString(), e.message, null, "error-0001"),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(UserAlreadyExistException::class)
    fun userAlreadyExistHandler(e: UserAlreadyExistException) = ResponseEntity(
        ErrorResponse("Username is already in use", HttpStatus.CONFLICT.value().toString(), e.message, null, "error-0002"),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(SendMailException::class)
    fun sendMailExceptionHandler(e: SendMailException) = ResponseEntity(
        ErrorResponse("Error Sending Mail", HttpStatus.INTERNAL_SERVER_ERROR.value().toString(), e.message),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

    @ExceptionHandler(Exception::class)
    fun sendMailExceptionHandler(e: Exception) = ResponseEntity(
        ErrorResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value().toString(), e.message),
        HttpStatus.INTERNAL_SERVER_ERROR
    )
}
