package io.github.x1111101101.glucoseserver.admin.route

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routeAdmins() {
    routing {
        route("admin") {
            routeFoods()
        }
    }
}

private fun Route.routeFoods() {
    route("food") {

    }
}