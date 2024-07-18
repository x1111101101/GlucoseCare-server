package io.github.x1111101101.food.model.classification

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object ClassificationSessionManager {

    private val sessions = ConcurrentHashMap<UUID, ClassificationSession>()
    private val workers = Array(5) {ClassificationWorker(MainScope())}

    fun startSession(image: ByteArray): UUID {
        val session = ClassificationSession(image)
        this.sessions[session.uuid] = session
        MainScope().launch {
            workers.random().addJob(session)
        }
        return session.uuid
    }

    fun getSession(uuid: UUID) = sessions[uuid]


}

