package io.github.x1111101101.glucoseserver.prescription.service

import com.google.auth.oauth2.GoogleCredentials
import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.prescription.model.ocr.OcrSessionManager
import java.io.File
import java.util.*


object PrescriptionOcrService {

    init {
        initGoogleCloud()
    }


    suspend fun readPlain(img: ByteArray): String {
        return OcrSessionManager.startSession(img).await().second
    }

    fun getSessionImage(uuid: UUID): ByteArray? {
        return OcrSessionManager.getSession(uuid)?.image
    }

}

private fun initGoogleCloud() {
    val keyFile = File(PROPERTIES["GOOGLE_APPLICATION_CREDENTIALS"].toString())
    GoogleCredentials.fromStream(keyFile.inputStream())
    System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", PROPERTIES["GOOGLE_APPLICATION_CREDENTIALS"].toString())
}
