package io.github.x1111101101.account.vo

import kotlinx.serialization.Serializable

@Serializable
data class UserVO(
    val name: String,
    val loginId: String,
    val phoneNumber: String,

) {

}