package com.task.teamreboott.common

import com.task.teamreboott.exception.NotExistFeatureException
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotExistFeatureException::class)
    fun handleNotExistFeatureException(e: NotExistFeatureException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(HttpStatus.NOT_FOUND.name, ErrorMessage.NON_EXIST_FEATURE))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(HttpStatus.NOT_FOUND.name, e.message ?: ErrorMessage.NOT_FOUND_ENTITY))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "잘못된 입력입니다.")
        }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handlePathValidation(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val errors = ex.constraintViolations.associate {
            val field = it.propertyPath.toString().substringAfterLast(".")
            field to (it.message ?: "잘못된 요청입니다.")
        }
        return ResponseEntity.badRequest().body(errors)
    }
}