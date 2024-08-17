package io.github.x1111101101.glucoseserver.food.dish.model

import io.github.x1111101101.glucoseserver.food.dish.model.nutrition.Nutrition

fun Nutrition.allWeight(): Double {
    return listOf(
        this.carbohydrate, this.salt/1000.0, this.fat, this.water, this.dietaryFiber, this.protein
    ).sum()
}