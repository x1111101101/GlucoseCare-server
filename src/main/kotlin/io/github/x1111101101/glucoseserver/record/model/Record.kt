package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable
import java.util.UUID


interface Record {
    val uuid: String
    val version: Long
    val createdTime: Long
    val deleted: Boolean
    val type: RecordType
}