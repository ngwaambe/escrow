package com.sicuro.escrow.controller

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.sicuro.escrow.exception.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

data class ErrorResponse(
    val title: String,
    val status: String,
    val validationErrors: MutableList<ValidationError>? = null,
    val code: String? = null
)

data class ValidationError(
    val name: String,
    val reason: String?
)

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun objectNotExistHandler(e: ObjectNotFoundException) = ResponseEntity(
        ErrorResponse("Entity not found", HttpStatus.NOT_FOUND.value().toString()),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(UsernameNotFoundException::class, BadCredentialsException::class)
    fun usernameNotFoundExceptionHandler(e: AuthenticationException) = ResponseEntity(
        ErrorResponse("Entity not found", HttpStatus.NOT_FOUND.value().toString()),
        HttpStatus.NOT_FOUND
    )

    @ExceptionHandler(ObjectAlreadyExistException::class)
    fun objectAlreadyExistHandler(e: ObjectAlreadyExistException) = ResponseEntity(
        ErrorResponse("Entity already exist", HttpStatus.CONFLICT.value().toString(), null, "error-0001"),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(UserAlreadyExistException::class)
    fun userAlreadyExistHandler(e: UserAlreadyExistException) = ResponseEntity(
        ErrorResponse("Username is already in use", HttpStatus.CONFLICT.value().toString(), null, "error-0002"),
        HttpStatus.CONFLICT
    )

    @ExceptionHandler(SendMailException::class)
    fun sendMailExceptionHandler(e: SendMailException) = ResponseEntity(
        ErrorResponse("Error Sending Mail", HttpStatus.INTERNAL_SERVER_ERROR.value().toString()),
        HttpStatus.INTERNAL_SERVER_ERROR
    )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException) = ResponseEntity(
        ErrorResponse(
            "Invalid Format",
            HttpStatus.BAD_REQUEST.value().toString(),
            makeErrorRespone(ex)
        ),
        HttpStatus.BAD_REQUEST
    )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleBadRequest(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        logger.warn(makeErrorMessage(ex))
        return ResponseEntity(
            ErrorResponse(
                "Data Validation error",
                HttpStatus.BAD_REQUEST.value().toString(),
                makeErrorResponse(ex)
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBadRequest(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.warn(makeErrorMessage(ex))
        return ResponseEntity(
            ErrorResponse(
                "Data Validation error",
                HttpStatus.BAD_REQUEST.value().toString(),
                makeErrorResponse(ex)
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(InvalidActivationLinkException::class)
    fun handleInvalidActivationLinkException(ex: InvalidActivationLinkException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                "Invalid Link",
                HttpStatus.NOT_FOUND.value().toString()
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(Exception::class)
    fun sendMailExceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        return ResponseEntity(
            ErrorResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value().toString()),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    private fun makeErrorResponse(ex: MethodArgumentNotValidException): MutableList<ValidationError> {
        val result = mutableListOf<ValidationError>()
        ex.bindingResult.fieldErrors.forEach {
            result.add(ValidationError(it.field, it.defaultMessage))
        }
        ex.bindingResult.globalErrors.forEach {
            result.add(ValidationError(it.objectName, it.defaultMessage))
        }
        return result
    }

    private fun makeErrorResponse(ex: ConstraintViolationException): MutableList<ValidationError> {
        val result = mutableListOf<ValidationError>()
        ex.constraintViolations.forEach {
            result.add(ValidationError(it.propertyPath.toString(), it.message))
        }
        return result
    }

    private fun makeErrorRespone(ex: HttpMessageNotReadableException): MutableList<ValidationError> {
        val result = mutableListOf<ValidationError>()
        if (ex.cause is MismatchedInputException) {
            val cause = ex.cause as MismatchedInputException
            val property = StringBuilder()
            cause.path.forEach {
                if (property.isEmpty())
                    property.append(it.fieldName)
                else property.append(".").append(it.fieldName)
            }
            result.add(ValidationError(property.toString(), cause.message))
        }
        return result
    }
    /**
     * Note: this is only used for logging well-readable output
     */
    private fun makeErrorMessage(ex: MethodArgumentNotValidException): String {

        val builder = StringBuffer()
            .append("Input data validation failed in controller method ${ex.parameter.method!!.name}() in ${ex.parameter.declaringClass.simpleName}\n")
            .append("Errors: [\n")
        ex.bindingResult.fieldErrors.forEach {
            builder.append("\t")
                .append("Field: ${it.field}, invalid value: ${it.rejectedValue}, message: ${it.defaultMessage}")
                .append("\t")
        }

        ex.bindingResult.globalErrors.forEach {
            builder
                .append("\t")
                .append("Object: ${it.objectName}, message: ${it.defaultMessage}")
                .append("\n")
        }
        builder.append("]")
        return builder.toString()
    }

    /**
     * Note: this is only used for logging well-readable output
     */
    private fun makeErrorMessage(ex: ConstraintViolationException): String {
        val builder = StringBuffer()
            .append("Errors: [\n")
        ex.constraintViolations.forEach {
            builder.append("\t")
                .append("Field: ${it.propertyPath}, invalid value: ${it.invalidValue}, message: ${it.message}")
                .append("\n")
        }
        builder.append("]")
        return builder.toString()
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }
}
