package io.github.x1111101101.glucoseserver.record.database.entity

import io.github.x1111101101.glucoseserver.record.model.Record
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecordWrap {

    val recordId: String
    val recordType: String
    val recordJsonBody: String
    val createdTime: Long
    val userId: String

    constructor(record: Record, userId: String) {
        this.userId = userId
        this.recordId = record.uuid
        this.recordType = record.type.name
        this.recordJsonBody = Json.encodeToString(record)
        this.createdTime = record.createdTime
    }

    constructor(recordId: String, recordType: String, recordJsonBody: String, userId: String, createdTime: Long) {
        this.recordId = recordId
        this.recordType = recordType
        this.recordJsonBody = recordJsonBody
        this.userId = userId
        this.createdTime = createdTime
    }

}