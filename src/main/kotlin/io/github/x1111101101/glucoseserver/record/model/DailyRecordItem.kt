package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DailyRecordItem(val uuid: String, val version: Long) {
}