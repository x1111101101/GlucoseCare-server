package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.BurnedCaloriesDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.StepsDataHolder
import kotlinx.serialization.Serializable

@Serializable
data class WalkingExtraData(
    override val stepCount: Int,
    override val burnedCalories: Double = stepCount * 0.03,
) : ExerciseExtraData(ExerciseType.WALKING), BurnedCaloriesDataHolder<WalkingExtraData>, StepsDataHolder<WalkingExtraData> {

    override fun copyWithBurnedCalories(burnedCalories: Double): WalkingExtraData {
        return copy(burnedCalories = burnedCalories)
    }

    override fun copyWithSteps(stepCount: Int): WalkingExtraData {
        return copy(stepCount = stepCount)
    }
}