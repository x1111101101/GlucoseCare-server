package io.github.x1111101101.glucoseserver.food.dish.model

import io.github.x1111101101.glucoseserver.food.dish.model.nutrition.Nutrition
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Dish(
    override val uuid: String = UUID.randomUUID().toString(),
    override val name: String,
    override val gramsPerServing: Double,
    /**  per 100g  */
    override val nutrition: Nutrition,
    val ingredients: List<IngredientItem>,
    val aliases: List<String> = emptyList(),
    val tags: List<String> = emptyList()
) : PerServingHolder, Food

@Serializable
data class IngredientItem(
    val ingredientUUID: String,
    /**  per 100g  */
    val weight: Double
)