package io.github.x1111101101.glucoseserver.record.model.medicine

enum class MedicineTiming(val display: String) {
    WAKEUP("기상 후"),
    BEFORE_BREAKFAST("아침 식사 전"), AFTER_BREAKFAST("아침 식사 후"),
    BEFORE_LAUNCH("점심 식사 전"), AFTER_LAUNCH("점심 식사 후"),
    BEFORE_DINNER("저녁 식사 전"), AFTER_DINNER("저녁 식사 후"),
    BEFORE_SLEEP("취침 전"),
}