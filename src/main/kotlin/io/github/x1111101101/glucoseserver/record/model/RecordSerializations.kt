package io.github.x1111101101.glucoseserver.record.model

import io.github.x1111101101.glucoseserver.record.model.glucose.TokGlucoseRecord
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val RECORD_SERIALIZERS_MODULE = SerializersModule {
    polymorphic(Record::class) {
        subclass(TokGlucoseRecord::class, TokGlucoseRecord.serializer())
    }
}