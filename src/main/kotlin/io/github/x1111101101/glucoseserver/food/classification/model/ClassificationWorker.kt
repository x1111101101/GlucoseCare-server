package io.github.x1111101101.glucoseserver.food.classification.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.food.classification.dto.FoodClassificationResult
import io.github.x1111101101.glucoseserver.food.dish.service.DishService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.MessageFormat
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

private val OPENAI_TOKEN = PROPERTIES["OPENAI_TOKEN"].toString()
private val HOST_ADDRESS = PROPERTIES["HOST_ADDRESS"].toString()
private val PROMPT = PROPERTIES["CLASSIFICATION_PROMPT"].toString()
private val VALIDATION_PROMPT = MessageFormat(PROPERTIES["CLASSIFICATION_VALIDATION_PROMPT"].toString())
private val VALIDATION_PROMPT_V2 = MessageFormat(PROPERTIES["CLASSIFICATION_VALIDATION_PROMPT_V2"].toString())

class ClassificationWorker(private val scope: CoroutineScope) {

    companion object {
        private val COMPRESS_GOAL = 1024 * 1024 * 2
    }

    private val client = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(30))
        .readTimeout(Duration.ofSeconds(30))
        .connectTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .connectionPool(ConnectionPool(5, 3L, TimeUnit.HOURS))
        .build()
    private val channel = Channel<ClassificationSession>(Channel.BUFFERED)

    fun start() = scope.launch {
        while (true) {
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
        val apiJson = promptJsonWithImage("gpt-4-turbo", PROMPT, imageUrl)
        println("IMG")
        val message = doPrompt(apiJson)
        try {
            // Query
            val root = JsonParser.parseString(message).asJsonArray
            val predictions = root.asList().map { it.asJsonArray }.map { outer ->
                outer.asList().map { it.asJsonPrimitive.asString }
            }
            val matchedPredictions = DishService.matchPredictions(predictions)
            session.result = matchedPredictions
            session.state = ClassificationSession.State.SUCCEED

            // Validation
            println("START VALIDATION")
            val namesFromOpenAi = matchedPredictions.map {
                it.predictions.firstOrNull()?.openAiName
            }.filterNotNull()
            val namesInDb = matchedPredictions.map {
                it.predictions.map { it.foodName }
            }
            val namesFromOpenAiJson = JsonArray().also { arr ->
                namesFromOpenAi.forEach { li ->
                    val innerArr = JsonArray()
                    arr.add(innerArr)
                    li.forEach { innerArr.add(it) }
                }
            }.toString()
            val namesInDbJson = JsonArray().also { arr ->
                namesInDb.forEach { li ->
                    li.forEach { arr.add(it) }
                }
            }.toString()
            val validationMessage = doPrompt(
                promptJson(
                    "gpt-4-turbo",
                    VALIDATION_PROMPT_V2.format(arrayOf(namesFromOpenAi.joinToString(", "), namesInDbJson))
                )
            )
            println("validationMessage: ${validationMessage}")
            val toRemove = JsonParser.parseString(validationMessage).asJsonArray.map { it.asString }.toHashSet()
            val validPredictions = matchedPredictions.map {
                it.copy(predictions = it.predictions.filter { !toRemove.contains(it.foodName) })
            }
            session.state = ClassificationSession.State.SUCCEED_VALID
            session.result = validPredictions
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.state != ClassificationSession.State.SUCCEED)
                session.state = ClassificationSession.State.ERROR
            return
        }
    }

    private suspend fun doPrompt(apiJson: String): String {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), apiJson)
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
        println(apiJson)
        val openAiResponse = suspendCoroutine {
            try {
                println("Calling API: $apiJson")
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        it.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        println("OPEN AI: $body")
                        if (body == null) {
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
        val json = JsonParser.parseString(openAiResponse) as JsonObject
        val choices = json.get("choices") as JsonArray
        val message = choices.first().asJsonObject
            .getAsJsonObject("message")
            .getAsJsonPrimitive("content").asString
        return message
    }
}


private fun promptJsonWithImage(model: String, prompt: String, imageUrl: String): String {
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
      "max_tokens": 1300
       }
    """.trimIndent()
}

private fun promptJson(model: String, prompt: String): String {
    val root = JsonObject()
    root.addProperty("model", model)
    root.addProperty("max_tokens", 1300)
    root.add("messages", JsonArray().apply {
        add(JsonObject().apply {
            addProperty("role", "user")
            add("content", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("type", "text")
                    addProperty("text", prompt)
                })
            })
        })
    })
    return root.toString()
}

private fun compressJpegImage(inputFile: ByteArray, maxFileSizeInBytes: Int): ByteArray {
    val image: BufferedImage = ImageIO.read(inputFile.inputStream())
    var compressionQuality = 1.0f
    var fileSize: Int
    var bas: ByteArrayOutputStream
    do {
        bas = ByteArrayOutputStream()
        bas.use {
            ImageIO.createImageOutputStream(bas).use { ios ->
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
            }
        }
    } while (fileSize > maxFileSizeInBytes && compressionQuality > 0.0f)
    return bas.toByteArray()
}