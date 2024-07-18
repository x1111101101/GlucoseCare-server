package io.github.x1111101101

import io.github.x1111101101.food.route.routeFoods
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 5101) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    routing {
        get("") { call.respond("HI") }
    }
    routeFoods()
}