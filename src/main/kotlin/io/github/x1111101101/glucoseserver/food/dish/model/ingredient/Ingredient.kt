package io.github.x1111101101.glucoseserver.food.dish.model.ingredient

import io.github.x1111101101.glucoseserver.food.dish.model.Nutrition
import io.github.x1111101101.glucoseserver.food.dish.model.exchange.FoodExchange
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val uuid: String,
    val nutrition: Nutrition,
    val exchange: FoodExchange
) {

}