package io.github.x1111101101.glucoseserver.food.dish.model.ingredient

import io.github.x1111101101.glucoseserver.food.dish.model.Nutrition
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val uuid: String,
    val nutrition: Nutrition
) {

}