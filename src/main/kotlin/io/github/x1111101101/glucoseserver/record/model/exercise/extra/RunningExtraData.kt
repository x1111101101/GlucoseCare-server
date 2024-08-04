package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.BurnedCaloriesDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.DistanceDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.StepsDataHolder
import kotlinx.serialization.Serializable


@Serializable
data class RunningExtraData(
    override val burnedCalories: Int,
    override val distanceInMeters: Int,
) : ExerciseExtraData(ExerciseType.RUNNING), DistanceDataHolder, BurnedCaloriesDataHolder {

}