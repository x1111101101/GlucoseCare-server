package io.github.x1111101101.glucoseserver.food.model.classification

import io.github.x1111101101.glucoseserver.PROPERTIES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.Duration
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val OPENAI_TOKEN = PROPERTIES["OPENAI_TOKEN"].toString()
private val HOST_ADDRESS = PROPERTIES["HOST_ADDRESS"].toString()
private val PROMPT = PROPERTIES["CLASSIFICATION_PROMPT"].toString()

class ClassificationWorker(private val scope: CoroutineScope) {

    companion object {
        private val COMPRESS_GOAL = 1024*1024*2
    }

    private val client = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(30))
        .readTimeout(Duration.ofSeconds(30))
        .connectTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .build()
    private val channel = Channel<ClassificationSession>(Channel.BUFFERED)

    fun start() = scope.launch {
        while(true) {
            val session = channel.receive()
            try {
                work(session)
            } catch (e: Exception) {
                e.printStackTrace()
                session.state = ClassificationSession.State.ERROR
            }
        }
    }

    suspend fun addJob(session: ClassificationSession) {
        channel.send(session)
    }

    private suspend fun work(session: ClassificationSession) {
        session.image = compressJpegImage(session.image, COMPRESS_GOAL)
        session.state = ClassificationSession.State.SEND
        val imageUrl = "http://$HOST_ADDRESS/food/classification/session/image/${session.uuid}"
        val json = promptAIJson("gpt-4-turbo", PROMPT, imageUrl)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .headers(
                Headers.headersOf(
                    "Content-Type", "application/json",
                    "Authorization", "Bearer $OPENAI_TOKEN"
                )
            )
            .post(requestBody)
            .build()
        println(json)
        val respond = suspendCoroutine {
            try {
                println("Calling API")
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        it.resumeWithException(e)
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        println("OPEN AI: $body")
                        if(body == null) {
                            it.resumeWithException(IllegalStateException())
                            return
                        }
                        if (!response.isSuccessful) {
                            it.resumeWithException(IOException("Unexpected code $response, ${response.message}"))
                            return
                        }
                        it.resume(body)
                    }
                })
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
        }
        println("CALLED API")
        session.result = respond
        session.state = ClassificationSession.State.SUCCEED
    }
}

private fun promptAIJson(model: String, prompt: String, imageUrl: String): String {
    return """
        {
        "model": "$model",
        "messages": [
        {
          "role": "user",
          "content": [
            {
              "type": "text",
              "text": "$prompt"
            },
            {
              "type": "image_url",
              "image_url": {
                "url": "$imageUrl",
                "detail": "high"
              }
            }
          ]
        }
      ],
      "max_tokens": 300
       }
    """.trimIndent()
}

private fun compressJpegImage(inputFile: ByteArray, maxFileSizeInBytes: Int): ByteArray {
    val image: BufferedImage = ImageIO.read(inputFile.inputStream())
    var compressionQuality = 1.0f
    var fileSize: Int
    var bas: ByteArrayOutputStream
    do {
        bas = ByteArrayOutputStream()
        bas.use {
            ImageIO.createImageOutputStream(bas).use { ios->
                val writer: ImageWriter = ImageIO.getImageWritersByFormatName("jpg").next()
                writer.output = ios
                val param: ImageWriteParam = writer.defaultWriteParam.also {
                    it.compressionMode = ImageWriteParam.MODE_EXPLICIT
                    it.compressionQuality = compressionQuality
                }
                writer.write(null, IIOImage(image, null, null), param)
                writer.dispose()
                fileSize = bas.size()
                compressionQuality -= 0.05f
            }}
    } while (fileSize > maxFileSizeInBytes && compressionQuality > 0.0f)
    return bas.toByteArray()
}