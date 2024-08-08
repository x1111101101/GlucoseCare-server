package io.github.x1111101101.glucoseserver.record.model.exercise.extra.dataholder

interface DistanceDataHolder<T> {
    val distanceInMeters: Int

    fun copyWithDistance(distanceInMeters: Int): T
}