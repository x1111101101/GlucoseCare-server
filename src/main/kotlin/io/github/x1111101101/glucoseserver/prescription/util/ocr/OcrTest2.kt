package io.github.x1111101101.glucoseserver.prescription.util.ocr

import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.TextAnnotation
import com.google.protobuf.ByteString
import java.io.FileInputStream

fun processPrescriptionImage(imagePath: String) {
    ImageAnnotatorClient.create().use { vision ->
        // Load the image
        val img = Image.newBuilder().setContent(getImageContent(imagePath)).build()

        // Set OCR as the feature
        val feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()

        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(feature)
            .setImage(img)
            .build()

        // Perform OCR
        val response = vision.batchAnnotateImages(listOf(request))
        val annotation = response.responsesList.first().fullTextAnnotation

        // Process text for medication details
        extractMedicationDetails(annotation)
    }
}

// Helper function to convert image to ByteString
fun getImageContent(imagePath: String): ByteString {
    return ByteString.readFrom(FileInputStream(imagePath))
}

fun extractMedicationDetails(annotation: TextAnnotation) {
    val text = annotation.text
    val medicationDetails = mutableListOf<Medication>()

    // Example parsing logic (this would be tailored to prescription format)
    val lines = text.split("\n")
    lines.forEach { line ->
        val medication = parseLineForMedication(line)
        if (medication != null) medicationDetails.add(medication)
    }

    // Output parsed details
    medicationDetails.forEach { println(it) }
}

// Parse each line for medication name, dose, and quantity
fun parseLineForMedication(line: String): Medication? {
    // Implement regex or parsing rules based on prescription structure
    val namePattern = Regex("")
    val dosePattern = Regex("")
    val quantityPattern = Regex("")

    val name = namePattern.find(line)?.value
    val dose = dosePattern.find(line)?.value
    val quantity = quantityPattern.find(line)?.value

    return if (name != null && dose != null && quantity != null) {
        Medication(name, dose, quantity)
    } else null
}

data class Medication(val name: String, val dose: String, val quantity: String)