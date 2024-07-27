package io.github.x1111101101.glucoseserver.record.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordIdRequest(val recordUUID: String) {
}