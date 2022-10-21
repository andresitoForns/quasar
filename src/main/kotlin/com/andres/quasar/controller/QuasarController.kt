package com.andres.quasar.controller

import com.andres.quasar.logic.QuasarLogic
import com.andres.quasar.model.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler
    fun handle(e: Exception, request: WebRequest) : ResponseEntity<Any>{
        return ResponseEntity(e.message, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

}

@RestController
class QuasarController {

    private val logic: QuasarLogic = QuasarLogic()

    @PostMapping("/topsecret")
    fun topSecret(@RequestBody param: SatelliteBodyParam): SatelliteLocateResponse {
        return logic.locateSatellite(param.satellites) ?: throw NotFoundException()
    }

    @PostMapping("/topsecret_split/{satellite_name}")
    fun createSatellite(
        @RequestBody param: SatelliteSplitBodyParam,
        @PathVariable satellite_name: String
    ): SatelliteCreatedResponse? {
        if (param.distance == null) {
            return null
        }
        return logic.addSatelliteData(Satellite(satellite_name, param.distance, param.message))
            ?: throw NotFoundException()
    }

    @GetMapping("/topsecret_split")
    fun resolveSatelliteLocation(): SatelliteLocateResponse? {
        return logic.resolveSatelliteLocation() ?: throw NotEnoughInfoException()
    }
}

class NotFoundException : Exception("No encontrado")

class NotEnoughInfoException : Exception("No hay suficiente información.")
