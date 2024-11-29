package io.github.x1111101101.glucoseserver.prescription.model.internal.ocr

import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import com.google.protobuf.Descriptors.FieldDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object OcrSessionManager {

    private val sessions = ConcurrentHashMap<UUID, OcrSession>()


    fun startSession(image: ByteArray): Deferred<Pair<UUID, String>> {
        val session = OcrSession(image)
        sessions[session.uuid] = session
        return CoroutineScope(Dispatchers.IO).async {
            val result = session.uuid to extractTextFromImage(image)
            terminateSession(session.uuid)
            result
        }
    }



    fun terminateSession(uuid: UUID) = sessions.remove(uuid)

    fun getSession(uuid: UUID) = sessions[uuid]


}

fun callGVO(image: ByteArray, type: Feature.Type = Feature.Type.DOCUMENT_TEXT_DETECTION): BatchAnnotateImagesResponse {
    val img: Image = Image.newBuilder().setContent(ByteString.copyFrom(image)).build()
    val feat: Feature = Feature.newBuilder().setType(type).build()
    val request: AnnotateImageRequest = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build()
    val requests: MutableList<AnnotateImageRequest> = ArrayList<AnnotateImageRequest>()
    requests.add(request)
    return ImageAnnotatorClient.create().use { client ->
        val response: BatchAnnotateImagesResponse = client.batchAnnotateImages(requests)
        response
    }
}

@Throws(Exception::class)
fun extractTextFromImage(image: ByteArray): String {
    val img: Image = Image.newBuilder().setContent(ByteString.copyFrom(image)).build()
    val feat: Feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build()
    val request: AnnotateImageRequest = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build()
    val requests: MutableList<AnnotateImageRequest> = ArrayList<AnnotateImageRequest>()
    requests.add(request)
    ImageAnnotatorClient.create().use { client ->
        val response: BatchAnnotateImagesResponse = client.batchAnnotateImages(requests)

        val stringBuilder = StringBuilder()
        for (res in response.getResponsesList()) {
            if (res.hasError()) {
                System.out.printf("Error: %s\n", res.getError().getMessage())
                return "Error detected"
            }
            stringBuilder.append(res.getFullTextAnnotation().getText())
        }
        return stringBuilder.toString()
    }
}