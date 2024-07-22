package io.github.x1111101101.account.model

data class UserRegister(
    val loginId: String,
    val loginPasswordHash: String,
    val name: String,
    val phoneNumber: String,
    val birthday: Long
) {
}