package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import kotlinx.serialization.Serializable

@Serializable
open class ExerciseExtraData(val exerciseType: ExerciseType) {

}