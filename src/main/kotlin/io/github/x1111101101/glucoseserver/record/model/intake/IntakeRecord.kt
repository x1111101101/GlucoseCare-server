package io.github.x1111101101.glucoseserver.record.model.intake

import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.Serializable

@Serializable
data class IntakeRecord(
    override val date: Int,
    override val deleted: Boolean = false,
    override val uuid: String,
    override val version: Long,
    val intakeType: IntakeType,
    val items: List<IntakeItem>

): Record {
    override val type: RecordType = RecordType.INTAKE
}

@Serializable
data class IntakeItem(val foodUUID: String, val unit: IntakeUnit, val grams: Double)

enum class IntakeUnit {
    SERVING, GRAM, AMOUNT
}