package io.github.x1111101101.glucoseserver.record.model.exercise.extra

import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseType
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.BurnedCaloriesDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.DistanceDataHolder
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder.StepsDataHolder

data class WalkingExtraData(
    override val stepCount: Int,
    override val burnedCalories: Int,
    override val distanceInMeters: Int,
) : ExerciseExtraData(ExerciseType.WALKING), DistanceDataHolder, BurnedCaloriesDataHolder, StepsDataHolder {

}