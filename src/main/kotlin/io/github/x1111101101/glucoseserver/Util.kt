package io.github.x1111101101.glucoseserver

import org.jetbrains.exposed.sql.Database
import org.threeten.bp.Instant
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

fun currentMillis(): Long {
    return Instant.now().toEpochMilli()
}

fun isValidSHA256(hash: String): Boolean {
    val sha256Regex = Regex("^[a-fA-F0-9]{64}$")
    return sha256Regex.matches(hash)
}

fun connectDB() {
    Database.connect("jdbc:sqlite:local.db", driver = "org.sqlite.JDBC")
}

fun sha256Hash(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return DatatypeConverter.printHexBinary(hashBytes).lowercase()
}