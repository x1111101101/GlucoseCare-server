package io.github.x1111101101.glucoseserver.medicine.model

import java.util.UUID

class OcrSession(
    val image: ByteArray,
    val uuid: UUID = UUID.randomUUID()
) {
    var result: String? = null
}