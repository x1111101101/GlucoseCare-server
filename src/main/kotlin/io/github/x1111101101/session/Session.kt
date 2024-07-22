package io.github.x1111101101.session

import java.time.Instant
import java.util.*

data class Session(val uuid: UUID, val userId: Int, val createdTime: Instant) {

}