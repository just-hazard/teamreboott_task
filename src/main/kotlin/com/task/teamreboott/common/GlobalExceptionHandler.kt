package com.task.teamreboott.common

import com.task.teamreboott.exception.NotExistFeatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotExistFeatureException::class)
    fun handleNotExistFeatureException(e: NotExistFeatureException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(HttpStatus.NOT_FOUND.name, ErrorMessage.NON_EXIST_FEATURE))
    }
}