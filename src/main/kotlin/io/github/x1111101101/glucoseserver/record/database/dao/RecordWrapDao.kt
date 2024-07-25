package io.github.x1111101101.glucoseserver.record.database.dao

import io.github.x1111101101.glucoseserver.record.database.entity.RecordWrap
import io.github.x1111101101.glucoseserver.record.database.table.RecordWrapTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RecordWrapDao {

    init {
        transaction {
            SchemaUtils.create(RecordWrapTable)
        }
    }

    fun insert(r: RecordWrap) {
        transaction {
            RecordWrapTable.insert {
                it[type] = r.recordType
                it[jsonBody] = r.recordJsonBody
                it[id] = UUID.fromString(r.recordId)
                it[userId] = r.userId
            }
        }
    }

    fun delete(recordId: UUID): Boolean {
        return transaction {
            RecordWrapTable.deleteWhere { (RecordWrapTable.id eq recordId) } > 0
        }
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
            RecordWrap(row[id].value.toString(), row[type], row[jsonBody], row[userId])
        }
    }

}