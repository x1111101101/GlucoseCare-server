package io.github.x1111101101.glucoseserver.account.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val loginId: String,
    val loginPasswordHash: String,
    val phoneNumber: String,
    val name: String,
    val extra: String,
    val birthDay: Long
) {

}