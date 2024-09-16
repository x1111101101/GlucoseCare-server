package io.github.x1111101101.glucoseserver.record.model.medicine

import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineRecord(
    override val deleted: Boolean,
    override val uuid: String,
    override val version: Long,
    override val date: Int,
    override val time: Int,
    val timing: MedicineTiming,
    val prescriptionId: String = "",
    val medicines: List<Pair<Medicine, MedicineVolume>>
): Record {
    @SerialName("recordType")
    override val recordType: RecordType get() = RecordType.TAKE_MEDICINE
}