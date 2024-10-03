package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.BurnedCaloriesDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.DistanceDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.StepsDataHolder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("RunningExtraData")
@Serializable
data class RunningExtraData(
    override val burnedCalories: Double,
    override val distanceInMeters: Int,
) : ExerciseExtraData, DistanceDataHolder<RunningExtraData>, BurnedCaloriesDataHolder<RunningExtraData> {

    override val exerciseType: ExerciseType
        get() = ExerciseType.RUNNING

    override fun copyWithDistance(distanceInMeters: Int): RunningExtraData {
        return copy(distanceInMeters = distanceInMeters)
    }

    override fun copyWithBurnedCalories(burnedCalories: Double): RunningExtraData {
        return copy(burnedCalories = burnedCalories)
    }
}