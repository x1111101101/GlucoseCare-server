package io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder

interface StepsDataHolder<T> {
    val stepCount: Int

    fun copyWithSteps(stepCount: Int): T
}