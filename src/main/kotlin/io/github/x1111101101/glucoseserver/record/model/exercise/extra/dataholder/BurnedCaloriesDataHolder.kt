package io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder

interface BurnedCaloriesDataHolder<T> {
    val burnedCalories: Double

    fun copyWithBurnedCalories(burnedCalories: Double): T
}