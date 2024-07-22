package io.github.x1111101101.account.repository

import io.github.x1111101101.account.entity.User
import io.github.x1111101101.account.model.UserRegister
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager

private object Users : IntIdTable() {
    val name = varchar("name", 10)
    val loginId = varchar("loginId", 10)
    val loginPasswordHash = varchar("pwHash", 100)
    val phoneNumber = varchar("phoneNumber", 15)
    val extra = text("extra")
}

private fun initUserRepository() {
    Database.connect("jdbc:sqlite:local.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(Users)
    }
}

private fun ResultRow.toUser(): User{
    return User(
        id = this[Users.id].value,
        loginId = this[Users.loginId],
        loginPasswordHash = this[Users.loginPasswordHash],
        phoneNumber = this[Users.phoneNumber]
    )
}

class UserRepository {

    init {
        initUserRepository()
    }

    fun addUser(userRegister: UserRegister): User? {
        var userId: Int? = null
        transaction {
            userId = Users.insertAndGetId {
                it[Users.name] = name
                it[Users.loginId] = loginId
            }.value
        }
        return userId?.let { getUser(it) }
    }

    fun getUser(id: Int): User? {
        return transaction {
            Users.select { Users.id eq id }
                .map { it.toUser() }
                .singleOrNull()
        }
    }

    fun getAllUsers(): List<User> {
        return transaction {
            Users.selectAll().map {
                it.toUser()
            }
        }
    }

    fun deleteUser(id: Int): Boolean {
        return transaction {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }
}
