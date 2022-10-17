package com.andres.quasar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuasarApplication

fun main(args: Array<String>) {
	runApplication<QuasarApplication>(*args)
}
