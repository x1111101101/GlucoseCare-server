package io.github.x1111101101.glucoseserver.session

import io.github.x1111101101.glucoseserver.account.entity.User
import java.util.UUID

object SessionManager {

    private val repository = SessionRepository()

    fun create(loginId: String): Session {
        return repository.createSession(loginId)
    }

    operator fun get(sessionId: UUID): Session? {
        return repository.getSession(sessionId)
    }

}