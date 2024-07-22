package io.github.x1111101101.account.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val loginId: String,
    val loginPasswordHash: String,
    val phoneNumber: String
)