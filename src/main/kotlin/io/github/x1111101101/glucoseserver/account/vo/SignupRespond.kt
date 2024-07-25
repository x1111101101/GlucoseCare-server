package io.github.x1111101101.glucoseserver.account.vo

import kotlinx.serialization.Serializable

@Serializable
data class SignupRespond(val succeed: Boolean, val message: String) {

}