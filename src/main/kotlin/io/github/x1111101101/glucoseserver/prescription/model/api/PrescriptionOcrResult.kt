package io.github.x1111101101.glucoseserver.prescription.model.api

import io.github.x1111101101.glucoseserver.record.model.medicine.MedicineTiming
import io.github.x1111101101.glucoseserver.record.model.medicine.MedicineVolume
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionOcrResult(
    val items: List<MedicineItemParseResult>
) {
}

@Serializable
data class MedicineItemParseResult(
    val name: String,
    val code: Int,
    val timings: Set<MedicineTiming>,
    val volume: MedicineVolume
)