package com.andres.quasar.logic

import com.andres.quasar.model.Satellite
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

class SatelliteBox {
    private var satellites: HashMap<String, Satellite> = HashMap()

    fun putSatellite (satellite: Satellite) {
        val name = satellite.name!!.lowercase()
        satellites[name] = satellite
    }

    fun getSatellitesMap () : HashMap<String, Satellite>{
        return satellites
    }

    @Bean
    @Scope("singleton")
    fun satelliteBox(): SatelliteBox {
        return SatelliteBox()
    }
}