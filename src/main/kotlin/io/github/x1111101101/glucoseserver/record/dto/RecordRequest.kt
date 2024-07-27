package io.github.x1111101101.glucoseserver.record.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordRequest(
    val recordJson: String,
    val recordType: String
) {
}