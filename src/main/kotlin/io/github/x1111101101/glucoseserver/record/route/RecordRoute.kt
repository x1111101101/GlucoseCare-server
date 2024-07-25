package io.github.x1111101101.glucoseserver.record.route

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routeRecords() {
    routing {
        routeRecords()
    }
}

private fun Route.routeRecords() {
    route("record") {

    }
}