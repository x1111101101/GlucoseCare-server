package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable

@Serializable
enum class RecordType {
    GLUCOSE, EXERCISE_SESSION, PRESCRIPTION, TAKE_MEDICINE
}