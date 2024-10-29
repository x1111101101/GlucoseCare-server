package io.github.x1111101101.glucoseserver.prescription.route

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.prescription.model.internal.ocr.callGVO
import io.github.x1111101101.glucoseserver.prescription.model.internal.ocr.extractTextFromImage
import io.github.x1111101101.glucoseserver.prescription.service.PrescriptionOcrService
import io.github.x1111101101.glucoseserver.prescription.util.ocr.GoogleVisionLineSegmentationParser
import io.github.x1111101101.glucoseserver.prescription.util.ocr.OcrTest1
import io.github.x1111101101.glucoseserver.prescription.util.ocr.parsePrescription
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
    route("ocrtest") {
        get {
            var f = call.request.queryParameters["f"].toString().replace("\n", "/")
            println(f)
            val imageFile = File(PROPERTIES["TEST_FOLDER"].toString(), f)
            println(imageFile)
            if(!imageFile.isFile) {
                println("file not found")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val text = extractTextFromImage(imageFile.readBytes())
            //println(text)
            call.respondText(text)
            println("parsed:")
            parsePrescription(text)
        }
    }
    route("ocrtest2") {
        get {
            var f = call.request.queryParameters["f"].toString().replace("\n", "/")
            println(f)
            val imageFile = File(PROPERTIES["TEST_FOLDER"].toString(), f)
            println(imageFile)
            if(!imageFile.isFile) {
                println("file not found")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = callGVO(imageFile.readBytes())
            val parser = GoogleVisionLineSegmentationParser()
            println("count: ${response.responsesCount}")
            val sb = StringBuilder()
            OcrTest1.test(response.responsesList[0])
            sb.appendLine(response.responsesList.map {
                it.fullTextAnnotation.text
            }.joinToString("\n"))
            sb.appendLine("-------------------------------------------------")
            parser.initLineSegmentation(response.responsesList.first()).joinToString("\n").let {
                sb.appendLine(it)
            }
            sb.appendLine("-------------------------------------------------")
            val json = response.toString()
            sb.append(json)
            call.respondText(sb.toString())
        }
    }

}


