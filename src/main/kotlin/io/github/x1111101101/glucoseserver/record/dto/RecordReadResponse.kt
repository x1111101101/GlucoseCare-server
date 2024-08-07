package io.github.x1111101101.glucoseserver.record.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordReadResponse(
    val succeed: Boolean,
    val msg: String,
    val recordJson: String,
) {

}