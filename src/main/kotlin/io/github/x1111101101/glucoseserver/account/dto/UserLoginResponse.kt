package io.github.x1111101101.glucoseserver.account.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponse(val succeed: Boolean, val message: String, val sessionId: String?, val user: UserDTO?) {
}