package io.github.x1111101101.glucoseserver.record.service

import io.github.x1111101101.glucoseserver.R
import io.github.x1111101101.glucoseserver.account.entity.User
import io.github.x1111101101.glucoseserver.account.repository.UserRepository
import io.github.x1111101101.glucoseserver.record.database.entity.RecordWrap
import io.github.x1111101101.glucoseserver.record.database.repository.RecordRepository
import io.github.x1111101101.glucoseserver.record.dto.*
import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import java.util.UUID

object RecordsService {

    val userRepository: UserRepository = UserRepository()
    val recordRepository = RecordRepository()

    fun getRecord(loginId: String, request: RecordIdRequest): RecordReadRespond {
        val user = userRepository.getUserByLoginId(loginId)
            ?: return RecordReadRespond(false, R.strings.unknownUser, "")
        val recordId = UUID.fromString(request.recordUUID)

    }

    fun createRecord(loginId: String, recordRequest: RecordRequest): RecordCreateRespond {
        val user = userRepository.getUserByLoginId(loginId)
            ?: return RecordCreateRespond(false, R.strings.unknownUser)
        val record = deserializeRecord(recordRequest)
        val wrap = RecordWrap(record, loginId)

    }

    fun updateRecord(loginId: String, recordRequest: RecordRequest): RecordUpdateRespond {
        TODO()
    }

    fun deleteRecord(loginId: String, recordId: UUID) {

    }

    private fun isValid(user: User, record: Record) {

    }

    private fun deserializeRecord(request: RecordRequest): Record {
        when (RecordType.valueOf(request.recordType)) {
            RecordType.GLUCOSE -> return TODO()
            else -> throw IllegalArgumentException("unknown record type")
        }
    }

}