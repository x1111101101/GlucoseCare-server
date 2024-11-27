package io.github.x1111101101.glucoseserver.prescription.route

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.prescription.service.PrescriptionOcrService
import io.github.x1111101101.glucoseserver.prescription.util.ocr.OcrTest3
import io.github.x1111101101.glucoseserver.prescription.util.ocr.PrescriptionOcrUtil
import io.ktor.http.*
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
    route("ocrtest3") {
        get {
            var f = call.request.queryParameters["f"].toString().replace("\n", "/")
            println(f)
            val imageFile = File(PROPERTIES["TEST_FOLDER"].toString(), f)
            println(imageFile)
            if (!imageFile.isFile) {
                println("file not found")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            OcrTest3.test(imageFile)

        }
    }
    route("ocrtest4") {
        get {
            var f = call.request.queryParameters["f"].toString().replace("\n", "/")
            println(f)
            val imageFile = File(PROPERTIES["TEST_FOLDER"].toString(), f)
            println(imageFile)
            if (!imageFile.isFile) {
                println("file not found")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            PrescriptionOcrUtil.parse(imageFile)
        }
    }

}


