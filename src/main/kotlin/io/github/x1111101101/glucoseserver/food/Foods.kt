package io.github.x1111101101.glucoseserver.food

import io.github.x1111101101.glucoseserver.food.dish.model.Dish
import io.github.x1111101101.glucoseserver.food.dish.model.Food
import io.github.x1111101101.glucoseserver.food.dish.model.ingredient.Ingredient
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val FOOD_SERIALIZERS_MODULE = SerializersModule {
    polymorphic(Food::class) {
        subclass(Ingredient::class, Ingredient.serializer())
        subclass(Dish::class, Dish.serializer())
    }
}
val FOOD_JSON = Json {
    serializersModule = io.github.x1111101101.glucoseserver.food.dish.model.FOOD_SERIALIZERS_MODULE
}