package io.github.x1111101101.glucoseserver.record.data.table

import org.jetbrains.exposed.dao.id.IntIdTable

object DailyRecordsTable: IntIdTable() {
    val userId = varchar("userId", 20).index()
    val startOfDay = long("date").index()
    val records = text("records")
}