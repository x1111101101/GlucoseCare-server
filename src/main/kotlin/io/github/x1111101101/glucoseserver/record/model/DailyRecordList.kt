package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyRecordList(
    val items: List<DailyRecordItem>
) {
}