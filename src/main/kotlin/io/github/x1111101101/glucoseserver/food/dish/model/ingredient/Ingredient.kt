package io.github.x1111101101.glucoseserver.food.dish.model.ingredient

import io.github.x1111101101.glucoseserver.food.dish.Food
import io.github.x1111101101.glucoseserver.food.dish.model.nutrition.Nutrition
import io.github.x1111101101.glucoseserver.food.dish.model.exchange.FoodExchange
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    override val uuid: String,
    override val name: String,
    override val nutrition: Nutrition,
    val exchange: FoodExchange
): Food {

}