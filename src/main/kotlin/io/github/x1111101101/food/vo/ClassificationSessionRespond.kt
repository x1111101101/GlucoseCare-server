package io.github.x1111101101.food.vo

import com.google.gson.JsonObject

data class ClassificationSessionRespond(val uuid: String, val state: String, val result: String) {

    fun json() = JsonObject().apply {
        addProperty("uuid", uuid)
        addProperty("state", state)
        addProperty("result", result)
    }

}