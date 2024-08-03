package io.github.x1111101101.glucoseserver.record.model.medicine

import kotlinx.serialization.Serializable

@Serializable
data class Medicine(val name: String, val description: String = "") {

}