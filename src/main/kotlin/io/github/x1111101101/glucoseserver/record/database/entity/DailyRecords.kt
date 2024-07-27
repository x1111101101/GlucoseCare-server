package io.github.x1111101101.glucoseserver.record.database.entity

import io.github.x1111101101.glucoseserver.record.model.DailyRecordList


data class DailyRecords(
    val id: Int,
    val userId: String,
    val startOfDay: Long,
    val records: DailyRecordList
)