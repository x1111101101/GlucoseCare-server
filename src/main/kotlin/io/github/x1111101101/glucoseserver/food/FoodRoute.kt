package io.github.x1111101101.glucoseserver.food

import io.github.x1111101101.glucoseserver.food.classification.route.routeClassifications
import io.github.x1111101101.glucoseserver.food.dish.route.routeDishes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.routeFoods() {
    routing {
        route("food") {
            routeClassifications()
            routeDishes()
        }
    }
}
