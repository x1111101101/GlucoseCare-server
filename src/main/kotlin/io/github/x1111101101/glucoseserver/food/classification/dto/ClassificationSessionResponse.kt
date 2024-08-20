package io.github.x1111101101.glucoseserver.food.classification.dto

import io.github.x1111101101.glucoseserver.food.classification.model.ClassificationSession
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationSessionResponse(
    val sessionUUID: String,
    val state: FoodClassificationSessionState,
    val result: List<FoodClassificationResult>
)

@Serializable
data class FoodClassificationResult(val predictions: List<FoodPrediction>)

@Serializable
data class FoodPrediction(val openAiName: String, val foodUUID: String, val foodName: String)

enum class FoodClassificationSessionState {
    READY, SEND, SUCCEED, ERROR
}