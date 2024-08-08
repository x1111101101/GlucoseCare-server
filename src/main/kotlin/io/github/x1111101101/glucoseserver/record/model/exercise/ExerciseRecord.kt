package io.github.x1111101101.glucoseserver.record.model.exercise

import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.ExerciseExtraData
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ExerciseRecord(
    override val deleted: Boolean = false,
    override val uuid: String = UUID.randomUUID().toString(),
    override val version: Long,
    override val date: Int,
    val time: Int,
    val durationInSeconds: Int,
    val extraData: ExerciseExtraData,
    val tags: List<String> = emptyList()
): Record {
    override val type: RecordType get() = RecordType.EXERCISE_SESSION
}