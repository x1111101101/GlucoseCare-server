package io.github.x1111101101.glucoseserver.food.dish.model.api

import io.github.x1111101101.glucoseserver.food.dish.model.api.nutrition.Nutrition

interface Food {
    val name: String
    val uuid: String
    val nutrition: Nutrition
}