package io.github.x1111101101.glucoseserver.food.dish.model

import kotlinx.serialization.Serializable

@Serializable
data class Nutrition(
    val calories: Double,
    val water: Double,
    val carbohydrate: Double,
    val protein: Double,
    val salt: Double,
    val fat: Double,
) {

    fun multiply(ratio: Double): Nutrition {
        return Nutrition(
            calories = calories * ratio,
            carbohydrate = carbohydrate * ratio,
            protein = protein * ratio,
            salt = salt * ratio,
            fat = fat * ratio,
            water = water * ratio,
        )
    }

}