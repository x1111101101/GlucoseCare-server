package io.github.x1111101101.glucoseserver.record.model

import kotlinx.serialization.Serializable

@Serializable
data class Timeline (
    val start: Long,
    val end: Long
) {

}