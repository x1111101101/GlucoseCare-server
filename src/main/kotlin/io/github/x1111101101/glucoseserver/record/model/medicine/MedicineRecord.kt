package io.github.x1111101101.glucoseserver.record.model.medicine

import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.Serializable

@Serializable
data class MedicineRecord(
    override val deleted: Boolean,
    override val uuid: String,
    override val version: Long,
    override val date: Int,
    val time: Int,
    
): Record {
    override val type: RecordType get() = RecordType.TAKE_MEDICINE
}