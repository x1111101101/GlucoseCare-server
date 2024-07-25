package io.github.x1111101101.glucoseserver.account.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(val loginId: String, val loginPasswordHash: String) {
}