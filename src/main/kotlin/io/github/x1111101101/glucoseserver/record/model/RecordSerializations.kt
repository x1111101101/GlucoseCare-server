package io.github.x1111101101.glucoseserver.record.model

import io.github.x1111101101.glucoseserver.record.model.exercise.extra.ExerciseExtraData
import io.github.x1111101101.glucoseserver.record.model.exercise.ExerciseRecord
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.RunningExtraData
import io.github.x1111101101.glucoseserver.record.model.exercise.extra.WalkingExtraData
import io.github.x1111101101.glucoseserver.record.model.glucose.TokGlucoseRecord
import io.github.x1111101101.glucoseserver.record.model.medicine.MedicineRecord
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val RECORD_SERIALIZERS_MODULE = SerializersModule {
    polymorphic(Record::class) {
        subclass(TokGlucoseRecord::class, TokGlucoseRecord.serializer())
        subclass(MedicineRecord::class, MedicineRecord.serializer())
        subclass(ExerciseRecord::class, ExerciseRecord.serializer())
    }
    polymorphic(ExerciseExtraData::class) {
        subclass(WalkingExtraData::class, WalkingExtraData.serializer())
        subclass(RunningExtraData::class, RunningExtraData.serializer())
    }
}