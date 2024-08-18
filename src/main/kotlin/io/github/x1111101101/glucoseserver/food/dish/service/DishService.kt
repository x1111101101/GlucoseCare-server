package io.github.x1111101101.glucoseserver.food.dish.service

import io.github.x1111101101.glucoseserver.PROPERTIES
import io.github.x1111101101.glucoseserver.TEMP_SCOPE
import io.github.x1111101101.glucoseserver.food.FOOD_JSON
import io.github.x1111101101.glucoseserver.food.dish.database.dao.DishDao
import io.github.x1111101101.glucoseserver.food.dish.database.entity.DishEntity
import io.github.x1111101101.glucoseserver.food.dish.dto.FoodSearchCompleteResponse
import io.github.x1111101101.glucoseserver.food.dish.dto.FoodSearchCompletionItem
import io.github.x1111101101.glucoseserver.food.dish.model.api.Food
import io.github.x1111101101.glucoseserver.food.dish.model.api.ingredient.Ingredient
import io.github.x1111101101.glucoseserver.food.dish.repository.DishRepository
import io.github.x1111101101.glucoseserver.food.dish.util.SimpleNameSearchHelper
import io.github.x1111101101.glucoseserver.food.dish.util.allWeight
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import java.io.File
import java.util.*

object DishService {

    private val repository = DishRepository(DishDao())
    private val searchHelper = SimpleNameSearchHelper()

    init {
        TEMP_SCOPE.launch {
            println("init DishService")
            initFoods()
            initSearch()
        }
    }

    suspend fun getAllFoodJsons() = repository.getAll().map { it.dishJson }
    suspend fun getAllFoods() = getAllFoodJsons().map { FOOD_JSON.decodeFromString<Food>(it) }

    suspend fun completeSearch(query: String): FoodSearchCompleteResponse {
        return FoodSearchCompleteResponse(
            searchHelper.recommend(query).map { FoodSearchCompletionItem(it.uuid.toString(), it.name) }
        )
    }

    suspend fun findFood(id: UUID): String? {
        return repository.get(id)?.dishJson
    }

    suspend fun initFoods() {
        if (repository.getAll().isNotEmpty()) {
            return
        }
        val folder = File(PROPERTIES["INITIAL_FOOD_JSONS"]!!.toString())
        val foods = folder.listFiles()?.map { file ->
            try {
                FOOD_JSON.decodeFromString<Food>(file.readText())
            } catch (e: Exception) {
                e.printStackTrace()
                return@map null
            }
        }?.filterNotNull() ?: emptyList()
        println("InitFoods: ${foods.size}")
        val normalized = foods.map { food ->
            if (food is Ingredient) {
                val weight = food.nutrition.allWeight()
                if (weight == 0.0) {
                    println("weight is zero: $food")
                    return@map food
                }
                val newNutrition = food.nutrition.multiply(100.0 / food.nutrition.allWeight())
                return@map food.copy(nutrition = newNutrition)
            }
            food
        }
        normalized.map {
            repository.create(DishEntity(UUID.fromString(it.uuid), FOOD_JSON.encodeToString(it)))
        }
    }

    private suspend fun initSearch() {
        getAllFoods().forEach {
            searchHelper.add(UUID.fromString(it.uuid), it.name)
        }
    }

}