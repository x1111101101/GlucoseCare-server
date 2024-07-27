package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TokGlucoseRecord(
    override val version: Long,
    override val uuid: String,
    override val createdTime: Long,
    override val deleted: Boolean,
    val value: Int,
    val time: Long
): Record {

    override val type: RecordType
        get() = RecordType.GLUCOSE

}