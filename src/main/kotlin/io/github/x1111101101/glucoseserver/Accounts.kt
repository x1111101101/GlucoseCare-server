package io.github.x1111101101.glucoseserver

import io.github.x1111101101.glucoseserver.session.SessionManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import java.util.*

suspend fun PipelineContext<Unit, ApplicationCall>.getLogin(): String? {
    val sessionId = call.request.header("sid")
    if(sessionId == null) {
        call.respond(HttpStatusCode.Unauthorized, R.strings.loginRequired)
        return null
    }
    val session = SessionManager[UUID.fromString(sessionId)]
    if(session == null) {
        call.respond(HttpStatusCode.BadRequest, R.strings.sessionExpired)
        return null
    }
    return session.userLoginId
}