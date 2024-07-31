package io.github.x1111101101.glucoseserver.food

import io.github.x1111101101.glucoseserver.food.classification.route.foodClassificationRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routeFoods() {
    routing {
        route("food") {
            foodClassificationRoute()
        }
    }
}
