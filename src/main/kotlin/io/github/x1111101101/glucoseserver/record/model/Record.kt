package io.github.x1111101101.glucoseserver.record.model

import io.github.x1111101101.glucoseserver.millisToLocalDateTime
import java.time.LocalDateTime


interface Record {
    val uuid: String
    val version: Long
    val startOfDay: Long
    val deleted: Boolean
    val type: RecordType
}

fun Record.startOfDayAsLocalDateTime(): LocalDateTime {
    return millisToLocalDateTime(this.startOfDay)
}