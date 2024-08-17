package io.github.x1111101101.glucoseserver.food.exchange

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface MLServerApiService {
    @POST("predict")
    suspend fun predict(@Body body: RequestBody): String
}