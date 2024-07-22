package io.github.x1111101101.medicine.route

import io.github.x1111101101.medicine.service.PrescriptionOcrService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import java.util.*

fun Application.routeMedicines() {
    routing {
        route("medicine") {
            routePrescriptionOcr()
        }
    }
}

private fun Route.routePrescriptionOcr() {
    route("prescription") {
        post("ocr") {
            val image = call.receiveText().decodeBase64Bytes()
            call.respondText(PrescriptionOcrService.readPlain(image))
        }
        get("image/{uuid}") {
            val uuid = UUID.fromString(call.parameters["uuid"])
            val image = PrescriptionOcrService.getSessionImage(uuid)
            if(image == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respondBytes(image, ContentType.Image.Any)
        }
    }

}
