package io.github.x1111101101.glucoseserver.food.dish.model

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val uuid: String,
    val name: String,
    override val gramsPerServing: Double,
    val nutrition: Nutrition
): PerServingHolder