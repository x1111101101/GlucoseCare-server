package io.github.x1111101101.session

import io.github.x1111101101.account.entity.User

object SessionManager {

    private val repository = SessionRepository()

    fun create(loginId: String): Session {
        return repository.createSession(loginId)
    }

}