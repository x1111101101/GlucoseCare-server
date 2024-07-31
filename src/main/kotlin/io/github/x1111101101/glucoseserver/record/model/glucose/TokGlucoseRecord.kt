package io.github.x1111101101.glucoseserver.record.model.glucose

import io.github.x1111101101.glucoseserver.record.model.DayTimeHolder
import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class TokGlucoseRecord(
    override val version: Long,
    override val uuid: String,
    override val date: Int,
    override val deleted: Boolean,
    override val time: Int,
    val value: Int,
    val timing: String
): Record, DayTimeHolder {

    override val type: RecordType
        get() = RecordType.GLUCOSE



    companion object {
        const val TIMING_AFTER_INTAKE = "AFTER_INTAKE"
        const val TIMING_BEFORE_INTAKE = "BEFORE_INTAKE"
        const val TIMING_UNKNOWN = "UNKNOWN"
    }

}