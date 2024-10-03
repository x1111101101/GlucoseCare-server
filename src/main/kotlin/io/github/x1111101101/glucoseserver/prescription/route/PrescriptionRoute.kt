package io.github.x1111101101.glucoseserver.prescription.route

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.prescription.model.ocr.extractTextFromImage
import io.github.x1111101101.glucoseserver.prescription.service.PrescriptionOcrService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import java.io.File

fun Application.routePrescriptions() {
    routing {
        route("prescription") {
            routePrescriptionOcr()
        }
    }
}

private fun Route.routePrescriptionOcr() {
    route("ocr") {
        post {
            val image = call.receiveText().decodeBase64Bytes()
            call.respondText(PrescriptionOcrService.readPlain(image))
        }
    }
    route("ocrtest/{f}") {
        initGoogleCloud()
        get {
            var f = call.parameters["f"].toString().trim('\"')
            f = f.decodeBase64String()
            println(f)
            val imageFile = File(f)
            if(!imageFile.isFile) return@get
            val text = extractTextFromImage(imageFile.readBytes())
            println(text)
            call.respondText(text)
        }
    }

}

private fun initGoogleCloud() {
    System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", PROPERTIES["GOOGLE_APPLICATION_CREDENTIALS"].toString())
}
