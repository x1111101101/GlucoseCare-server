package io.github.x1111101101.glucoseserver.record.database.repository

import io.github.x1111101101.glucoseserver.record.database.dao.RecordWrapDao
import io.github.x1111101101.glucoseserver.record.model.Record

/**
 * sync DailyRecordsTable and RecordWrapTable
 */
class RecordRepository(
    private val recordWrapDao: RecordWrapDao = RecordWrapDao()
) {

    fun create(record: Record) {

    }

}