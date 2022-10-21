package com.andres.quasar.model

import com.google.gson.annotations.SerializedName
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class SatelliteBodyParam {
    @SerializedName("satellites")
    var satellites: ArrayList<Satellite> = arrayListOf()
}

class SatelliteSplitBodyParam {
    @SerializedName("distance")
    var distance: Double? = null
    @SerializedName("message")
    var message: ArrayList<String> = arrayListOf()
}

@ResponseStatus(value = HttpStatus.ACCEPTED)
class SatelliteLocateResponse(
    var position: Position,
    var message: String
)

@ResponseStatus(value = HttpStatus.CREATED)
class SatelliteCreatedResponse(
    var satellite: Satellite
)

class Position(
    var x: Double,
    var y: Double
)

data class Satellite(
    @SerializedName("name") var name: String? = null,
    @SerializedName("distance") var distance: Double? = null,
    @SerializedName("message") var message: ArrayList<String> = arrayListOf()
)