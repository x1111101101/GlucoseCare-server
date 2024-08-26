package io.github.x1111101101.glucoseserver.record.model.intake

import io.github.x1111101101.glucoseserver.food.dish.model.api.nutrition.Nutrition
import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class IntakeRecord(
    override val date: Int,
    override val deleted: Boolean = false,
    override val uuid: String = UUID.randomUUID().toString(),
    override val version: Long,
    override val time: Int,
    val intakeType: IntakeType,
    val items: List<IntakeItem> = emptyList(),
    val totalNutrition: Nutrition
): Record {
    override val type: RecordType = RecordType.INTAKE
}

@Serializable
data class IntakeItem(val foodUUID: String, val unit: IntakeUnit, val grams: Double)

enum class IntakeUnit {
    SERVING, GRAM, AMOUNT
}