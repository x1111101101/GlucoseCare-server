package io.github.x1111101101.glucoseserver.record.model


interface Record: DayTimeHolder {
    val uuid: String
    val version: Long
    val date: Int
    val deleted: Boolean
    val type: RecordType
}