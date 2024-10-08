package io.github.x1111101101.glucoseserver.food.classification.route

import io.github.x1111101101.glucoseserver.food.classification.service.FoodClassificationService
import io.github.x1111101101.glucoseserver.getLogin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Route.routeClassifications() {
    route("classification") {
        sessionRoute()
    }
}

private fun Route.sessionRoute() {
    route("session") {
        get("state/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            val respond = FoodClassificationService.getSessionState(uuid)
            call.respondText(Json.encodeToString(respond), ContentType.Application.Json)
        }
        get("image/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            val image = FoodClassificationService.getSessionImage(uuid)
            if (image == null) {
                call.respond(HttpStatusCode.NoContent, "session not found")
                return@get
            }
            call.respondBytes(image, ContentType.Image.Any, HttpStatusCode.OK)
        }
        post("create") {
            if (getLogin() == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            val image = call.receiveText().decodeBase64Bytes()
            val uuid = FoodClassificationService.createSession(image)
            call.respondText(uuid.toString())
        }
    }
}