package io.github.x1111101101

import io.github.x1111101101.food.route.routeFoods
import io.github.x1111101101.medicine.route.routeMedicines
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    init()
    embeddedServer(Netty, port = 5101) {
        module()
    }.start(wait = true)
}

private fun init() {
    initGoogleCloud()
}

private fun initGoogleCloud() {
    System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", PROPERTIES["GOOGLE_APPLICATION_CREDENTIALS"].toString())
}

fun Application.module() {
    routing {
        get("") {
            call.respond("HI")
        }
    }
    routeFoods()
    routeMedicines()
}