package io.github.x1111101101.food.route

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routeFoods() {
    routing {
        route("food") {
            foodClassificationRoute()
        }
    }
}
