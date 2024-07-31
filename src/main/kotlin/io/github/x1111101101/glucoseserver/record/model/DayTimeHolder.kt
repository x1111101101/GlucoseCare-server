package io.github.x1111101101.glucoseserver.record.model

import java.time.LocalTime

interface DayTimeHolder {

    val time: Int // seconds

    fun timeAsLocalTime(): LocalTime {
        return LocalTime.of(0, 0).plusSeconds(time.toLong())
    }

}