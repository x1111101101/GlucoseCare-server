package io.github.x1111101101.glucoseserver.food.dish.model

import io.github.x1111101101.glucoseserver.food.dish.model.ingredient.Ingredient
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val FOOD_SERIALIZERS_MODULE = SerializersModule {
    polymorphic(Food::class) {
        subclass(Ingredient::class, Ingredient.serializer())
        subclass(Dish::class, Dish.serializer())
    }
}