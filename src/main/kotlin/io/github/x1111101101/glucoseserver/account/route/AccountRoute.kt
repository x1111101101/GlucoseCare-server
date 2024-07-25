package io.github.x1111101101.glucoseserver.account.route

import io.github.x1111101101.glucoseserver.account.dto.UserRegister
import io.github.x1111101101.glucoseserver.account.service.UserService
import io.github.x1111101101.glucoseserver.account.dto.UserLoginRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.routeAccounts() {
    routing {
        route("account") {
            routeUsers()
        }
    }
}

private fun Route.routeUsers() {
    route("user") {
        post("signup") {
            val base64 = call.receiveText()
            val json = base64.decodeBase64Bytes().decodeToString()
            val userRegister: UserRegister = Json.decodeFromString(json)
            val respond = UserService.signup(userRegister)
            call.respondText(Json.encodeToString(respond))
        }
        post("login") {
            val json = call.receiveText().decodeBase64Bytes().decodeToString()
            val request: UserLoginRequest = Json.decodeFromString(json)
            val respond = UserService.signin(request)
            call.respondText(Json.encodeToString(respond))
        }
    }
}