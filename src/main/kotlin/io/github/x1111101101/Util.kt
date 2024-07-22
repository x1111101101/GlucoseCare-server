package io.github.x1111101101

import org.threeten.bp.Instant

fun currentMillis(): Long {
    return Instant.now().toEpochMilli()
}