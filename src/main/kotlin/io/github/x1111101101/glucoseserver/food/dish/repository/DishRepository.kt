package io.github.x1111101101.glucoseserver.food.dish.repository

import io.github.x1111101101.glucoseserver.food.dish.database.dao.DishDao
import io.github.x1111101101.glucoseserver.food.dish.database.entity.DishEntity
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class DishRepository(
    private val dao: DishDao
) {

    private val state = MutableStateFlow(null)

    suspend fun create(entity: DishEntity) {
        dao.create(entity)
    }

    suspend fun update(entity: DishEntity) {
        dao.update(entity)
    }

    suspend fun get(uuid: UUID): DishEntity? {
        return dao.get(uuid)
    }

    suspend fun getAll(): List<DishEntity> = dao.getAll()

}