package io.github.x1111101101.glucoseserver

import io.github.x1111101101.glucoseserver.record.model.Record
import org.jetbrains.exposed.sql.Database
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.xml.bind.DatatypeConverter

val DEFAULT_ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)

fun currentMillis(): Long {
    return Instant.now().toEpochMilli()
}

fun fromMillis(millis: Long): Instant {
    return Instant.ofEpochMilli(millis)
}

fun millisToLocalDateTime(millis: Long): LocalDateTime {
    val instant = fromMillis(millis)
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun LocalDateTime.getStartOfDayMillis(): Long {
    return this.toLocalDate().atStartOfDay().toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli()
}

fun isValidSHA256(hash: String): Boolean {
    val sha256Regex = Regex("^[a-fA-F0-9]{64}$")
    return sha256Regex.matches(hash)
}

fun connectDB() {
    Database.connect("jdbc:sqlite:local.db", driver = "org.sqlite.JDBC")
}

internal fun sha256Hash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return DatatypeConverter.printHexBinary(hashBytes).lowercase()
}