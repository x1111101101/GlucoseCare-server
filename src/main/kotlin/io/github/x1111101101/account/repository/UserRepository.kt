package io.github.x1111101101.account.repository

import io.github.x1111101101.account.entity.User
import io.github.x1111101101.account.vo.UserRegister
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

private object Users : IntIdTable() {
    val name = varchar("name", 10)
    val loginId = varchar("loginId", 20)
    val loginPasswordHash = varchar("pwHash", 100)
    val phoneNumber = varchar("phoneNumber", 15)
    val extra = text("extra")
    val birthday = long("birthday")
}

private fun initUserRepository() {
    transaction {
        SchemaUtils.create(Users)
    }
}

private fun ResultRow.toUser(): User {
    return User(
        id = this[Users.id].value,
        loginId = this[Users.loginId],
        loginPasswordHash = this[Users.loginPasswordHash],
        phoneNumber = this[Users.phoneNumber],
        extra = this[Users.extra],
        name = this[Users.name],
        birthDay = this[Users.birthday]
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
                it[name] = userRegister.name
                it[loginId] = userRegister.loginId
                it[loginPasswordHash] = userRegister.loginPasswordHash
                it[extra] = userRegister.extra
                it[phoneNumber] = userRegister.phoneNumber
                it[birthday] = userRegister.birthday
            }.value
        }
        return userId?.let { getUser(it) }
    }

    fun getUserByLoginId(loginId: String): User? = transaction {
        Users.select { (Users.loginId eq loginId) }
            .map { it.toUser() }
            .singleOrNull()
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
