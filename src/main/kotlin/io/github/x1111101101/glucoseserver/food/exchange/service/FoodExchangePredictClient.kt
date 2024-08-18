package io.github.x1111101101.glucoseserver.food.exchange.service

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.food.dish.model.api.ingredient.Ingredient
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object FoodExchangePredictClient {

    private val BASE_URL = PROPERTIES["ML_SERVER_ADDRESS"]!!.toString()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val retrofitService = retrofit.create(MLServerApiService::class.java)

    suspend fun predict(ingredient: Ingredient) {
        val nutrition = ingredient.nutrition
        val request = nutrition.run { ApiRequest(
            carbohydrate, fat, protein, water, 100.0
        )
        }
        val response = retrofitService.predict(Json.encodeToString(request).toRequestBody("application/json".toMediaType()))
        println(response)
    }


    @Serializable
    private data class ApiRequest(val carbohydrate: Double, val fat: Double, val protein: Double, val water: Double, val weight: Double)

}