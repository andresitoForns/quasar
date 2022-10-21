package com.andres.quasar.logic

import com.andres.quasar.model.*
import com.lemmingapex.trilateration.TrilaterationFunction
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import kotlin.math.roundToInt


class QuasarLogic {
    private val positions = arrayOf(
        doubleArrayOf(-500.0, -200.0), //Kenobi
        doubleArrayOf(500.0, 100.0), //Sato
        doubleArrayOf(100.0, -100.0), //Skywalker
    )

    var applicationContext: ApplicationContext = ClassPathXmlApplicationContext("scopes.xml")

    fun locateSatellite(satellites: ArrayList<Satellite>): SatelliteLocateResponse? {
        satellites.sortBy { it.name }
        val distances = satellites.map { s -> s.distance }
        val distancesArray = DoubleArray(distances.size)
        for (i in distances.indices) distancesArray[i] = distances[i]!!

        val messages = satellites.map { s -> s.message.toTypedArray() }.toTypedArray()

        try {
            val location = getLocation(*distancesArray)
            val message = getMessage(*messages)

            if (location != null && location.size == 2) {
                return SatelliteLocateResponse(
                    position = Position(x = fixDecimals(location[0]), y = fixDecimals(location[1])),
                    message = message
                )
            }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    fun addSatelliteData(satellite: Satellite): SatelliteCreatedResponse? {
        if (satellite.name == null || satellite.distance == null) {
            return null
        }

        val box: SatelliteBox = applicationContext.getBean("satelliteBox") as SatelliteBox
        box.putSatellite(satellite)
//        (applicationContext as AbstractApplicationContext).close()
        return SatelliteCreatedResponse(satellite)
    }

    fun resolveSatelliteLocation(): SatelliteLocateResponse? {
        val box: SatelliteBox = applicationContext.getBean("satelliteBox") as SatelliteBox
        val map = box.getSatellitesMap()

        if (map.size < positions.size) {
            return null
        }

        val satellites = ArrayList(map.values)
        return locateSatellite(satellites)
    }

    private fun getLocation(vararg distances: Double): DoubleArray? {
        val centroid: DoubleArray?
        try {
            val solver =
                TrilaterationSolver(TrilaterationFunction(positions, distances), LevenbergMarquardtOptimizer())
            val optimum = solver.solve()

            centroid = optimum.point.toArray()
        } catch (e: Exception) {
            println(e)
            throw Exception("Some error occurred")
        }

        return centroid
    }

    private fun getMessage(vararg param: Array<String>): String {
        val result: StringBuilder = StringBuilder("")
        val messages = fixMessagesSource(param)
        for (j in messages[0].indices) {
            val temp = mutableListOf<String>()
            for (i in messages.indices) {
                if (messages[i][j].isNotBlank()) {
                    temp.add(messages[i][j])
                }
            }
            result.append(" ")
            result.append(temp.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: " ")
        }
        return result.toString().trim()
    }

    private fun fixMessagesSource(messages: Array<out Array<String>>): Array<out Array<String>> {
        if (messages.all { it.size == messages[0].size }) {
            return messages
        }
        val result = mutableListOf<Array<String>>()
        val desiredSize = messages.maxOf { it.size }
        for (message in messages) {
            val reversedMessage = message.reversed().toMutableList()
            while (reversedMessage.size < desiredSize) {
                reversedMessage.add("")
            }
            result.add(reversedMessage.reversed().toTypedArray())
        }
        return result.toTypedArray()
    }

    private fun fixDecimals(number: Double): Double {
        return (number * 100.0).roundToInt() / 100.0
    }
}