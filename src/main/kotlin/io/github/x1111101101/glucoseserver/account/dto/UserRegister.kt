package io.github.x1111101101.glucoseserver.account.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val loginId: String,
    val loginPasswordHash: String,
    val name: String,
    val phoneNumber: String,
    val birthday: Long,
    val extra: String
) {

}