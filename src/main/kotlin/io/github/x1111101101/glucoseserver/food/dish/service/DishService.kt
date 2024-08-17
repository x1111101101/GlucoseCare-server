package io.github.x1111101101.glucoseserver.food.dish.service

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.TEMP_SCOPE
import io.github.x1111101101.glucoseserver.food.FOOD_JSON
import io.github.x1111101101.glucoseserver.food.dish.database.dao.DishDao
import io.github.x1111101101.glucoseserver.food.dish.database.entity.DishEntity
import io.github.x1111101101.glucoseserver.food.dish.model.Food
import io.github.x1111101101.glucoseserver.food.dish.model.allWeight
import io.github.x1111101101.glucoseserver.food.dish.model.ingredient.Ingredient
import io.github.x1111101101.glucoseserver.food.dish.repository.DishRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import java.io.File
import java.util.UUID

object DishService {

    private val repository = DishRepository(DishDao())

    init {
        TEMP_SCOPE.launch {
            initFoods()
        }
    }

    suspend fun getDishes() = repository.getAll().map { it.dishJson }

    suspend fun initFoods() {
        if (repository.getAll().isNotEmpty()) {
            return
        }

        val folder = File(PROPERTIES["INITIAL_FOOD_JSONS"]!!.toString())
        val foods = folder.listFiles()?.map { file->
            try {
                FOOD_JSON.decodeFromString<Food>(file.readText())
            } catch (e: Exception) {
                return@map null
            }
        }?.filterNotNull() ?: emptyList()
        println("InitFoods: ${foods.size}")
        val normalized = foods.map { food->
            if(food is Ingredient) {
                val weight = food.nutrition.allWeight()
                if(weight == 0.0) {
                    println("weight is zero: $food")
                    return@map food
                }
                val newNutrition = food.nutrition.multiply(100.0/food.nutrition.allWeight())
                return@map food.copy(nutrition = newNutrition)
            }
            food
        }
        normalized.map {
            repository.create(DishEntity(UUID.fromString(it.uuid), FOOD_JSON.encodeToString(it)))
        }
    }

}