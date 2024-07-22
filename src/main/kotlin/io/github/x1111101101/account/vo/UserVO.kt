package io.github.x1111101101.account.vo

import io.github.x1111101101.account.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class UserVO(
    val name: String,
    val loginId: String,
    val phoneNumber: String,

) {

    constructor(user: User): this(
        name = user.name,
        loginId = user.loginId,
        phoneNumber = user.phoneNumber
    )

}