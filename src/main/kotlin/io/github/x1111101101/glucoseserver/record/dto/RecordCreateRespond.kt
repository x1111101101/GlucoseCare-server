package io.github.x1111101101.glucoseserver.record.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordCreateRespond(
    val succeed: Boolean,
    val msg: String
) {



}