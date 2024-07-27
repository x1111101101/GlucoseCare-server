package io.github.x1111101101.glucoseserver.record.model

import io.github.x1111101101.glucoseserver.fromMillis
import io.github.x1111101101.glucoseserver.millisToLocalDateTime
import java.time.LocalDateTime
import java.time.ZoneId


interface Record {
    val uuid: String
    val version: Long
    val createdTime: Long
    val deleted: Boolean
    val type: RecordType
}

fun Record.createdTimeAsLocalDateTime(): LocalDateTime {
    return millisToLocalDateTime(this.createdTime)
}