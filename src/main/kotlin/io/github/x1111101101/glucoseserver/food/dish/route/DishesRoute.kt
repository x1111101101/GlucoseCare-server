package io.github.x1111101101.glucoseserver.food.dish.route

import io.github.x1111101101.glucoseserver.food.dish.service.DishService
import io.github.x1111101101.glucoseserver.getLogin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun Route.routeDishes() {
    route("dish") {
        routeSearch()
    }
}

private fun Route.routeSearch() {
    route("search") {
        get("complete") {
            val login = getLogin()
            if(login == null) {
                call.respond(HttpStatusCode.Unauthorized, "로그인이 필요합니다.")
                return@get
            }
            val response = DishService.completeSearch(call.parameters.getOrFail("query"))
            call.respondText(Json.encodeToString(response))
        }
    }
}