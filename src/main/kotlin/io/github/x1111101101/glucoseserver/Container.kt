package io.github.x1111101101.glucoseserver

import io.github.x1111101101.glucoseserver.account.repository.UserRepository

object Container {

    val userRepository = UserRepository()

}