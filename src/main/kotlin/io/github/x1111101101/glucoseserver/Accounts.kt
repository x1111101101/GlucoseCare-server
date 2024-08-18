package io.github.x1111101101.glucoseserver

import io.github.x1111101101.glucoseserver.session.SessionManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import java.util.*

/**
 * get session id from request header 'sid' and check is valid
 * @return null - missing or wrong session id header
 */
suspend fun PipelineContext<Unit, ApplicationCall>.getLogin(): String? {
    if(IS_LOGIN_PASSED) return ""
    val sessionId = call.request.header("sid")
    if(sessionId == null) {
        call.respond(HttpStatusCode.Unauthorized, R.strings.LOGIN_REQUIRED)
        return null
    }
    val session = SessionManager[UUID.fromString(sessionId)]
    if(session == null) {
        call.respond(HttpStatusCode.BadRequest, R.strings.SESSION_EXPIRED)
        return null
    }
    return session.userLoginId
}