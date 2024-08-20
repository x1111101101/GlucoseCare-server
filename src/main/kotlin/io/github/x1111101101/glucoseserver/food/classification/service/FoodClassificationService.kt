package io.github.x1111101101.glucoseserver.food.classification.service

import io.github.x1111101101.glucoseserver.food.classification.model.ClassificationSessionManager
import io.github.x1111101101.glucoseserver.food.classification.dto.ClassificationSessionResponse
import java.util.UUID

object FoodClassificationService {

    fun createSession(image: ByteArray): UUID {
        return ClassificationSessionManager.startSession(image)
    }

    fun getSessionState(sessionId: UUID): ClassificationSessionResponse {
        val session = ClassificationSessionManager.getSession(sessionId)
        return session?.run {
            ClassificationSessionResponse(uuid.toString(), state.dtoEnum, result)
        } ?: throw IllegalStateException()
    }

    fun getSessionImage(sessionId: UUID): ByteArray? {
        return ClassificationSessionManager.getSession(sessionId)?.image
    }




}