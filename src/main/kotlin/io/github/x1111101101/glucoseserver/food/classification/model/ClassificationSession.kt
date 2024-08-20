package io.github.x1111101101.glucoseserver.food.classification.model

import io.github.x1111101101.glucoseserver.food.classification.dto.FoodClassificationResult
import io.github.x1111101101.glucoseserver.food.classification.dto.FoodClassificationSessionState
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ClassificationSession(var image: ByteArray) {
    val uuid = UUID.randomUUID()
    val created = LocalDateTime.now()
    var state = State.READY
    var result: List<FoodClassificationResult> = emptyList()

    enum class State(val dtoEnum: FoodClassificationSessionState) {
        READY(FoodClassificationSessionState.READY),
        SEND(FoodClassificationSessionState.SEND),
        SUCCEED(FoodClassificationSessionState.SUCCEED),
        ERROR(FoodClassificationSessionState.ERROR)
    }
}

