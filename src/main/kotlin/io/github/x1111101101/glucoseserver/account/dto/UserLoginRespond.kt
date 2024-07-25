package io.github.x1111101101.glucoseserver.account.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRespond(val succeed: Boolean, val message: String, val sessionId: String?, val user: UserDTO?) {
}