package io.github.x1111101101.glucoseserver.prescription.route

import io.github.x1111101101.glucoseserver.prescription.service.PrescriptionOcrService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

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

}
