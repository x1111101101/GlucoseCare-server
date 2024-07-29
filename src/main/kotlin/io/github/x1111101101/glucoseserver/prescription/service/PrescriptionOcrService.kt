package io.github.x1111101101.glucoseserver.prescription.service

import io.github.x1111101101.glucoseserver.prescription.model.ocr.OcrSessionManager
import java.util.*


object PrescriptionOcrService {


    suspend fun readPlain(img: ByteArray): String {
        return OcrSessionManager.startSession(img).await().second
    }

    fun getSessionImage(uuid: UUID): ByteArray? {
        return OcrSessionManager.getSession(uuid)?.image
    }

}

