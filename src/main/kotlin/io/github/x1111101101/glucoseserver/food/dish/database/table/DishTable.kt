package io.github.x1111101101.glucoseserver.food.dish.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object DishTable: UUIDTable() {

    val dishJson = text("dishJson")

}