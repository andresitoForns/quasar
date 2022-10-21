package com.andres.quasar.controller

import com.andres.quasar.logic.QuasarLogic
import com.andres.quasar.model.*
import org.springframework.web.bind.annotation.*

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
            throw MissingDataException()
        }
        return logic.addSatelliteData(Satellite(satellite_name, param.distance, param.message))
            ?: throw MissingDataException()
    }

    @GetMapping("/topsecret_split")
    fun resolveSatelliteLocation(): SatelliteLocateResponse? {
        return logic.resolveSatelliteLocation() ?: throw NotEnoughInfoException()
    }
}
