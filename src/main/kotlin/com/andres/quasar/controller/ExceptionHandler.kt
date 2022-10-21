package com.andres.quasar.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler
    fun handle(e: Exception, request: WebRequest) : ResponseEntity<Any> {
        return ResponseEntity(e.message, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

}

class NotFoundException : Exception("No encontrado")

class MissingDataException : Exception("Faltan parámetros obligatorios")

class NotEnoughInfoException : Exception("No hay suficiente información.")