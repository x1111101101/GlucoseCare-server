package io.github.x1111101101.glucoseserver.food.dish.model

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val uuid: String,
    val name: String,
    override val gramsPerServing: Double,
    /**
     * per 100 grams
     */
    val nutrition: Nutrition,
    val aliases: List<String>,
    val tags: List<String>
): PerServingHolder