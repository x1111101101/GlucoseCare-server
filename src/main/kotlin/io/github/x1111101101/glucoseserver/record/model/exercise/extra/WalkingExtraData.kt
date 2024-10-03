package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.AutoCollectedDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.BurnedCaloriesDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.StepsDataHolder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("WalkingExtraData")
@Serializable
data class WalkingExtraData(
    override val stepCount: Int,
    override val burnedCalories: Double = stepCount * 0.03,
    override val isAutoCollected: Boolean,
    val metadata: Map<String, String>
) : ExerciseExtraData, BurnedCaloriesDataHolder<WalkingExtraData>, StepsDataHolder<WalkingExtraData>, AutoCollectedDataHolder {

    override val exerciseType: ExerciseType
        get() = ExerciseType.WALKING

    override fun copyWithBurnedCalories(burnedCalories: Double): WalkingExtraData {
        return copy(burnedCalories = burnedCalories)
    }

    override fun copyWithSteps(stepCount: Int): WalkingExtraData {
        return copy(stepCount = stepCount)
    }
}