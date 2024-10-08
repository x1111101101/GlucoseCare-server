package io.github.x1111101101.glucoseserver

import io.github.x1111101101.glucoseserver.record.model.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import java.security.MessageDigest
import java.time.*
import java.util.UUID
import java.util.concurrent.Executors
import javax.xml.bind.DatatypeConverter

val DEFAULT_ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)

val TEMP_SCOPE = CoroutineScope(Executors.newFixedThreadPool(4).asCoroutineDispatcher())

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

fun LocalDate.getStartOfDayMillis(): Long {
    return this.atStartOfDay().toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli()
}

fun isValidSHA256(hash: String): Boolean {
    val sha256Regex = Regex("^[a-fA-F0-9]{64}$")
    return sha256Regex.matches(hash)
}

fun connectDB() {
    Database.connect("jdbc:sqlite:local.db", driver = "org.sqlite.JDBC")
}

fun connectTestDB() {
    Database.connect("jdbc:sqlite:localtest.db", driver = "org.sqlite.JDBC")
}

internal fun d(s: String) {
    println("DEBUG: $s")
}

internal fun sha256Hash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return DatatypeConverter.printHexBinary(hashBytes).lowercase()
}