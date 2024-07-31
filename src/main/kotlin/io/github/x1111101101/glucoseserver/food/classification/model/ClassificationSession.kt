package io.github.x1111101101.glucoseserver.food.classification.model

import java.time.LocalDateTime
import java.util.*

class ClassificationSession(var image: ByteArray) {
    val uuid = UUID.randomUUID()
    val created = LocalDateTime.now()
    var state = State.READY
    var result = ""

    enum class State {
        READY, SEND, SUCCEED, ERROR
    }
}

