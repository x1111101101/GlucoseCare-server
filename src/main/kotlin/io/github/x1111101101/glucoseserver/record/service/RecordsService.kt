package io.github.x1111101101.glucoseserver.record.service

import io.github.x1111101101.glucoseserver.R
import io.github.x1111101101.glucoseserver.account.entity.User
import io.github.x1111101101.glucoseserver.account.repository.UserRepository
import io.github.x1111101101.glucoseserver.record.database.entity.RecordWrap
import io.github.x1111101101.glucoseserver.record.repository.RecordRepository
import io.github.x1111101101.glucoseserver.record.dto.*
import io.github.x1111101101.glucoseserver.record.model.Record
import io.github.x1111101101.glucoseserver.record.model.RecordType
import java.util.UUID

object RecordsService {

    val userRepository: UserRepository = UserRepository()
    val recordRepository = RecordRepository()

    fun getRecord(loginId: String, request: RecordIdRequest): RecordReadResponse {
        val user = userRepository.getUserByLoginId(loginId)
            ?: return RecordReadResponse(false, R.strings.UNKNOWN_USER, "")
        val recordId = UUID.fromString(request.recordUUID)
        val recordWrap = recordRepository.get(recordId)
            ?: return RecordReadResponse(false, R.strings.RECORD_NOT_FOUND, "")
        if(!isValid(user, recordWrap)) {
            return RecordReadResponse(false, R.strings.INVALID_REQUEST, "")
        }
        return RecordReadResponse(true, "", recordWrap.recordJsonBody)
    }

    fun createRecord(loginId: String, recordRequest: RecordRequest): RecordCreateResponse {
        val user = userRepository.getUserByLoginId(loginId)
            ?: return RecordCreateResponse(false, R.strings.UNKNOWN_USER)
        val record = deserializeRecord(recordRequest)

        val succeed = recordRepository.create(record, user.loginId)
        return RecordCreateResponse(succeed, "")
    }

    fun updateRecord(loginId: String, recordRequest: RecordRequest): RecordUpdateResponse {
        TODO()
    }

    fun deleteRecord(loginId: String, recordId: UUID) {

    }

    private fun isValid(user: User, recordWrap: RecordWrap): Boolean {
        return user.loginId == recordWrap.userId
    }

    private fun deserializeRecord(request: RecordRequest): Record {
        when (RecordType.valueOf(request.recordType)) {
            RecordType.GLUCOSE -> return TODO()
            else -> throw IllegalArgumentException("unknown record type")
        }
    }

}