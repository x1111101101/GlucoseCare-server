package io.github.x1111101101.glucoseserver.session

import io.github.x1111101101.glucoseserver.currentMillis
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

private object Sessions: UUIDTable() {
    val userLoginId = varchar("userLoginId", 20)
    val createdTime = long("createdTime")
}

private fun initSessionTable() {
    transaction {
        SchemaUtils.create(Sessions)
    }
}

private fun ResultRow.toSession(): Session {
    return Session(
        uuid = this[Sessions.id].value,
        userLoginId = this[Sessions.userLoginId],
        createdTime = Instant.ofEpochMilli(this[Sessions.createdTime])
    )
}

class SessionRepository {

    init {
        initSessionTable()
    }

    fun createSession(userLoginId: String): Session {
        val id = UUID.randomUUID()
        transaction {
            Sessions.insert {
                it[Sessions.id] = id
                it[Sessions.userLoginId] = userLoginId
                it[createdTime] = currentMillis()
            }
        }
        return getSession(id) ?: throw IllegalStateException()
    }

    fun getSession(uuid: UUID): Session? {
        return transaction {
            Sessions.select { Sessions.id eq uuid }
                .map { it.toSession() }
                .singleOrNull()
        }
    }

}