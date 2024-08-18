package io.github.x1111101101.glucoseserver.food.dish.model.api.ingredient

import io.github.x1111101101.glucoseserver.food.dish.model.api.Food
import io.github.x1111101101.glucoseserver.food.dish.model.api.nutrition.Nutrition
import io.github.x1111101101.glucoseserver.food.dish.model.api.exchange.FoodExchange
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    override val uuid: String,
    override val name: String,
    override val nutrition: Nutrition,
    val exchange: FoodExchange
): Food {

}