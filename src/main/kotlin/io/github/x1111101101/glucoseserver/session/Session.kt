package io.github.x1111101101.glucoseserver.session

import java.time.Instant
import java.util.*

data class Session(val uuid: UUID, val userLoginId: String, val createdTime: Instant) {

}