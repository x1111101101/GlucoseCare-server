package io.github.x1111101101.glucoseserver.record.database.entity

import io.github.x1111101101.glucoseserver.record.model.Record
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RecordWrap {

    val recordId: String
    val recordType: String
    val recordJsonBody: String
    val userId: String

    constructor(record: Record, userId: String) {
        this.userId = userId
        recordId = record.uuid
        recordType = record.type.name
        recordJsonBody = Json.encodeToString(record)
    }

    constructor(recordId: String, recordType: String, recordJsonBody: String, userId: String) {
        this.recordId = recordJsonBody
        this.recordType = recordType
        this.recordJsonBody = recordJsonBody
        this.userId = userId
    }

}