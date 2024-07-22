package io.github.x1111101101

import org.threeten.bp.Instant

fun currentMillis(): Long {
    return Instant.now().toEpochMilli()
}

fun isValidSHA256(hash: String): Boolean {
    val sha256Regex = Regex("^[a-fA-F0-9]{64}$")
    return sha256Regex.matches(hash)
}