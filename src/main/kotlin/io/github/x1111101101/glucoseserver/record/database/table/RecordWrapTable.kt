package io.github.x1111101101.glucoseserver.record.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object RecordWrapTable: UUIDTable() {
    val jsonBody = text("jsonBody")
    val type = varchar("type", 10)
    val userId = varchar("userid", 20)
    val date = integer("date")
    val deleted = bool("deleted")
}