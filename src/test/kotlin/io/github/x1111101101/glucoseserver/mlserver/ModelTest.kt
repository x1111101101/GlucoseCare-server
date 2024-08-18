package io.github.x1111101101.glucoseserver.mlserver

import io.github.x1111101101.glucoseserver.TEMP_SCOPE
import io.github.x1111101101.glucoseserver.connectTestDB
import io.github.x1111101101.glucoseserver.food.FOOD_JSON
import io.github.x1111101101.glucoseserver.food.dish.model.api.Food
import io.github.x1111101101.glucoseserver.food.dish.model.api.ingredient.Ingredient
import io.github.x1111101101.glucoseserver.food.dish.service.DishService
import io.github.x1111101101.glucoseserver.food.exchange.service.FoodExchangePredictClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ModelTest {

    init {
        connectTestDB()
    }

    @Test
    fun test() {
        return
        runBlocking(TEMP_SCOPE.coroutineContext) {
            DishService.initFoods()
            val jsons = DishService.getAllFoodJsons().filter { it.contains("t.Ing") }
            val ingredients = jsons.map {
                FOOD_JSON.decodeFromString<Food>(it) as Ingredient
            }
            ingredients.forEach {
                FoodExchangePredictClient.predict(it)
                delay(500)
            }

        }
    }

}