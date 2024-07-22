package io.github.x1111101101.account.vo

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRespond(val succeed: Boolean, val message: String, val sessionId: String?, val user: UserVO?) {

}