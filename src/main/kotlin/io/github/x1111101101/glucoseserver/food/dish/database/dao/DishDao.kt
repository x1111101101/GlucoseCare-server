package io.github.x1111101101.glucoseserver.food.dish.database.dao

import io.github.x1111101101.glucoseserver.food.dish.database.entity.DishEntity
import io.github.x1111101101.glucoseserver.food.dish.database.table.DishTable
import io.github.x1111101101.glucoseserver.food.dish.model.Dish
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DishDao {

    init {
        transaction {
            SchemaUtils.create(DishTable)
        }
    }

    private fun ResultRow.toDishEntity(): DishEntity {
        return DishEntity(
            uuid = this[DishTable.id].value,
            dishJson = this[DishTable.dishJson]
        )
    }

    fun get(uuid: UUID): DishEntity? {
        return transaction {
            DishTable.select { DishTable.id eq uuid }.map {
                it.toDishEntity()
            }.singleOrNull()
        }
    }

    fun create(entity: DishEntity) {
        transaction {
            DishTable.insert {
                it[id] = entity.uuid
                it[dishJson] = entity.dishJson
            }
        }
    }

    fun update(entity: DishEntity) {
        transaction {
            DishTable.update(where = { DishTable.id eq entity.uuid }) {
                it[id] = entity.uuid
                it[dishJson] = entity.dishJson
            }
        }
    }

    fun delete(uuid: UUID) {
        transaction {
            DishTable.deleteWhere { DishTable.id eq uuid }
        }

    }

    fun getAll(): List<DishEntity> {
        return transaction {
            DishTable.selectAll().map { it.toDishEntity() }
        }
    }

}