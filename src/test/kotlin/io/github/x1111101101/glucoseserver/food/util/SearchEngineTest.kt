package io.github.x1111101101.glucoseserver.food.util

import io.github.x1111101101.glucoseserver.food.dish.util.SearchHelperItem
import io.github.x1111101101.glucoseserver.food.dish.util.SimpleNameSearchHelper
import org.junit.jupiter.api.Test
import java.util.UUID

class SearchEngineTest {

    @Test
    fun test() {
        val engine = SimpleNameSearchHelper()
        engine.add(UUID.randomUUID(), "시리얼, 옥수수, 코코아")
        println(engine.recommend("아몬"))
    }

}