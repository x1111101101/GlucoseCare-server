package io.github.x1111101101.glucoseserver.record.route

import io.github.x1111101101.glucoseserver.getLogin
import io.github.x1111101101.glucoseserver.record.dto.RecordIdRequest
import io.github.x1111101101.glucoseserver.record.dto.RecordRequest
import io.github.x1111101101.glucoseserver.record.service.RecordsService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.routeRecords() {
    routing {
        routeRecords()
    }
}

private fun Route.routeRecords() {
    route("records") {
        routeSingleRecord()
        routeDailyRecord()
    }
}

private fun Route.routeSingleRecord() {
    route("record") {
        get {
            val login = getLogin() ?: return@get
            val json = call.receiveText().decodeBase64Bytes().decodeToString()
            val respond = RecordsService.getRecord(login, Json.decodeFromString(json))
            call.respondText(Json.encodeToString(respond))
        }
        post {
            val login = getLogin() ?: return@post
            val json = call.receiveText().decodeBase64Bytes().decodeToString()
            val recordRequest = Json.decodeFromString<RecordRequest>(json)
            val respond = RecordsService.createRecord(login, recordRequest)
            call.respondText(Json.encodeToString(respond))
        }
    }
}

/**
 * creation, updating of DailyRecord is only for server-side
 */
private fun Route.routeDailyRecord() {
    route("daily") {
        get {
           val login = getLogin() ?: return@get

        }
    }
}