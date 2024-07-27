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
            SchemaUtils.create(RecordWrapTable)
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

    fun get(recordId: UUID): RecordWrap? {
        return transaction {
            RecordWrapTable.select { (RecordWrapTable.id eq recordId) }
                .map { it.toRecordWrap() }
                .firstOrNull()
        }
    }

    fun update(recordId: UUID, new: RecordWrap) {
        transaction {
            RecordWrapTable.update(where = { (RecordWrapTable.id eq recordId) }) {
                it[jsonBody] = new.recordJsonBody
            }
        }
    }

    private fun ResultRow.toRecordWrap(): RecordWrap {
        val row = this
        return RecordWrapTable.run {
            RecordWrap(row[id].value.toString(), row[type], row[jsonBody], row[userId], row[deleted])
        }
    }

}