package io.github.x1111101101.session

import io.github.x1111101101.currentMillis
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

private object Sessions: UUIDTable() {
    val userId = integer("userId")
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
        userId = this[Sessions.userId],
        createdTime = Instant.ofEpochMilli(this[Sessions.createdTime])
    )
}

class SessionRepository {

    fun createSession(userId: Int): Session? {
        val id = UUID.randomUUID()
        transaction {
            Sessions.insert {
                it[Sessions.id] = id
                it[Sessions.userId] = userId
                it[Sessions.createdTime] = currentMillis()
            }
        }
        return getSession(id)
    }

    fun getSession(uuid: UUID): Session? {
        return transaction {
            Sessions.select { Sessions.id eq uuid }
                .map { it.toSession() }
                .singleOrNull()
        }
    }

}