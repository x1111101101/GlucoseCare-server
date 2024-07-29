package io.github.x1111101101.glucoseserver.record.repository

import io.github.x1111101101.glucoseserver.getStartOfDayMillis
import io.github.x1111101101.glucoseserver.millisToLocalDateTime
import io.github.x1111101101.glucoseserver.record.database.dao.DailyRecordsDao
import io.github.x1111101101.glucoseserver.record.database.dao.RecordWrapDao
import io.github.x1111101101.glucoseserver.record.database.entity.DailyRecords
import io.github.x1111101101.glucoseserver.record.database.entity.RecordWrap
import io.github.x1111101101.glucoseserver.record.model.DailyRecordItem
import io.github.x1111101101.glucoseserver.record.model.DailyRecordList
import io.github.x1111101101.glucoseserver.record.model.Record
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * sync DailyRecordsTable and RecordWrapTable
 */
class RecordRepository(
    private val recordWrapDao: RecordWrapDao = RecordWrapDao(),
    private val dailyRecordsDao: DailyRecordsDao = DailyRecordsDao()
) {

    fun create(record: Record, userId: String): Boolean = transaction {
        val prev = recordWrapDao.get(UUID.fromString(record.uuid)) ?: return@transaction false
        val daily = getOrCreateDaily(userId, millisToLocalDateTime(record.startOfDay).getStartOfDayMillis())
        recordWrapDao.insert(RecordWrap(record, userId))
        val dailyRecordItems: List<DailyRecordItem> = daily.records.items + listOf(DailyRecordItem(record.uuid, record.version))
        if(!dailyRecordsDao.update(daily.id, DailyRecordList(dailyRecordItems))) {
            // TODO Rollback
            throw IllegalStateException()
        }
        return@transaction true
    }

    fun update(record: Record, userId: String) {
        recordWrapDao.update(UUID.fromString(record.uuid), RecordWrap(record, userId))
    }

    fun delete(recordId: UUID): Boolean = transaction {
        return@transaction recordWrapDao.delete(recordId)
    }


    fun get(recordId: UUID): RecordWrap? {
        return recordWrapDao.get(recordId)
    }

    private fun getOrCreateDaily(userId: String, startOfDay: Long): DailyRecords {
        return transaction {
            var current = dailyRecordsDao.get(userId, startOfDay)
            if (current == null) {
                dailyRecordsDao.insert(userId, DailyRecordList(emptyList()), startOfDay)
                current = dailyRecordsDao.get(userId, startOfDay) ?: throw IllegalStateException()
            }
            current
        }
    }


}