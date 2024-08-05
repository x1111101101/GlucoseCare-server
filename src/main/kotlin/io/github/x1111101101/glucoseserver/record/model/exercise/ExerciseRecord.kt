package io.github.x1111101101.glucoseserver.record.model.exercise

import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.ExerciseExtraData
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseRecord(
    override val deleted: Boolean,
    override val uuid: String,
    override val version: Long,
    override val date: Int,
    val time: Int,
    val durationInSeconds: Int,
    val extraData: ExerciseExtraData,
    val tags: List<String>
): Record {
    override val type: RecordType get() = RecordType.EXERCISE_SESSION
}