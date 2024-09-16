package io.github.x1111101101.glucoseserver.food.dish.model.api.nutrition

import kotlinx.serialization.Serializable

@Serializable
data class Nutrition(
    val calories: Double,
    val water: Double,
    val carbohydrate: Double,
    val protein: Double,
    val salt: Double,
    val fat: Double,
    val dietaryFiber: Double,
) {


    init {
        if(listOf(
            calories,
            water,
            carbohydrate,
            protein,
            salt,
            fat,
            dietaryFiber
        ).firstOrNull { it.isNaN() || it.isInfinite() } != null) {
            throw IllegalArgumentException("Nan or Infinite Value: $this")
        }

    }

    operator fun plus(other: Nutrition): Nutrition {
        return Nutrition(
            calories + other.calories,
            water + other.water,
            carbohydrate + other.carbohydrate,
            protein + other.protein,
            salt + other.salt,
            fat + other.fat,
            dietaryFiber + other.dietaryFiber
        )
    }

    operator fun times(ratio: Double) = multiply(ratio)

    fun multiply(ratio: Double): Nutrition {
        return Nutrition(
            calories = calories * ratio,
            carbohydrate = carbohydrate * ratio,
            protein = protein * ratio,
            salt = salt * ratio,
            fat = fat * ratio,
            water = water * ratio,
            dietaryFiber = dietaryFiber * ratio
        )
    }

    companion object {
        val EMPTY = Nutrition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }

}