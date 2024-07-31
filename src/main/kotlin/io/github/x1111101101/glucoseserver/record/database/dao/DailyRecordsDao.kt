package io.github.x1111101101.glucoseserver.record.database.dao

import io.github.x1111101101.glucoseserver.record.database.entity.DailyRecords
import io.github.x1111101101.glucoseserver.record.database.table.DailyRecordsTable
import io.github.x1111101101.glucoseserver.record.model.DailyRecordList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DailyRecordsDao {

    init {
        transaction {
            SchemaUtils.create(DailyRecordsTable)
        }
    }

    fun insert(userId: String, records: DailyRecordList, date: Int): Int {
        return transaction {
            DailyRecordsTable.insert {
                it[DailyRecordsTable.records] = Json.encodeToString(records)
                it[DailyRecordsTable.userId] = userId
                it[DailyRecordsTable.date] = date
            }
        }[DailyRecordsTable.id].value
    }

    fun get(userId: String, date: Int): DailyRecords? {
        return transaction {
            DailyRecordsTable
                .select { (DailyRecordsTable.date eq date) and (DailyRecordsTable.userId eq userId)}
                .map { it.toDailyRecords() }
                .firstOrNull()
        }
    }

    fun update(id: Int, list: DailyRecordList): Boolean {
        return transaction {
            DailyRecordsTable.update(where = { (DailyRecordsTable.id eq id) }) {
                it[records] = Json.encodeToString(list)
            }
        } >= 0
    }

    private fun ResultRow.toDailyRecords(): DailyRecords {
        val row = this
        return DailyRecordsTable.run {
            DailyRecords(row[id].value, row[userId], row[date], Json.decodeFromString(row[records]))
        }
    }

}