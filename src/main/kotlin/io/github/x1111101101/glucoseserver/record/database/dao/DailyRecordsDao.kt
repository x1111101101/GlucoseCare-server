package io.github.x1111101101.glucoseserver.record.database.dao

import io.github.x1111101101.glucoseserver.record.database.entity.DailyRecords
import io.github.x1111101101.glucoseserver.record.database.entity.RecordWrap
import io.github.x1111101101.glucoseserver.record.database.table.DailyRecordsTable
import io.github.x1111101101.glucoseserver.record.database.table.RecordWrapTable
import io.github.x1111101101.glucoseserver.record.model.DailyRecordList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DailyRecordsDao {

    init {
        transaction {
            SchemaUtils.create(DailyRecordsTable)
        }
    }

    fun insert(userId: String, records: DailyRecordList, startOfDay: Long): Int {
        return transaction {
            DailyRecordsTable.insert {
                it[DailyRecordsTable.records] = Json.encodeToString(records)
                it[DailyRecordsTable.userId] = userId
                it[DailyRecordsTable.startOfDay] = startOfDay
            }
        }[DailyRecordsTable.id].value
    }

    fun get(userId: String, startOfDay: Long): DailyRecords? {
        return transaction {
            DailyRecordsTable
                .select { (DailyRecordsTable.startOfDay eq startOfDay) and (DailyRecordsTable.userId eq userId)}
                .map { it.toDailyRecords() }
                .firstOrNull()
        }
    }

    fun update(id: Int, list: DailyRecordList) {
        transaction {
            DailyRecordsTable.update(where = { (DailyRecordsTable.id eq id) }) {
                it[records] = Json.encodeToString(list)
            }
        }
    }

    private fun ResultRow.toDailyRecords(): DailyRecords {
        val row = this
        return DailyRecordsTable.run {
            DailyRecords(row[id].value, row[userId], row[startOfDay], Json.decodeFromString(row[records]))
        }
    }

}