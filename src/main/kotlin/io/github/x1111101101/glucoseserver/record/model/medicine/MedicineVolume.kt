package io.github.x1111101101.glucoseserver.record.model.medicine

import kotlinx.serialization.Serializable

@Serializable
data class MedicineVolume(val unit: MedicineTakeUnit, val value: Float) {

}
