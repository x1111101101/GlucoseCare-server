package io.github.x1111101101.glucoseserver.food.dish.service

import io.github.x1111101101.glucoseserver.food.dish.database.dao.DishDao
import io.github.x1111101101.glucoseserver.food.dish.repository.DishRepository

object DishService {

    private val repository = DishRepository(DishDao())

    init {

    }



    private fun initDefaultDishes() {

    }

}