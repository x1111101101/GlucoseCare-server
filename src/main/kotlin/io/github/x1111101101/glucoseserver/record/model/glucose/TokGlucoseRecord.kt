package io.github.x1111101101.glucoseserver.record.model.glucose

import io.github.x1111101101.glucoseserver.record.model.DayTimeHolder
import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("GlucoseRecord")
@Serializable
data class TokGlucoseRecord(
    override val version: Long,
    override val uuid: String,
    override val date: Int,
    override val deleted: Boolean,
    override val time: Int,
    val value: Int,
    val timing: GlucoseRecordTiming
): Record, DayTimeHolder {

    @SerialName("recordType")
    override val recordType: RecordType
        get() = RecordType.GLUCOSE

}