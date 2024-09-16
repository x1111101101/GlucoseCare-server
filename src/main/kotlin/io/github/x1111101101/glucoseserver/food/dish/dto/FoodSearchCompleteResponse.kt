package io.github.x1111101101.glucoseserver.food.dish.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodSearchCompleteResponse(val result: List<FoodSearchCompletionItem>)

@Serializable
data class FoodSearchCompletionItem(val id: String, val name: String) {

}