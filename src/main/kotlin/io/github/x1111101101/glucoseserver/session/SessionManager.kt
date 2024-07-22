package io.github.x1111101101.glucoseserver.session

import io.github.x1111101101.glucoseserver.account.entity.User

object SessionManager {

    private val repository = SessionRepository()

    fun create(loginId: String): Session {
        return repository.createSession(loginId)
    }

}