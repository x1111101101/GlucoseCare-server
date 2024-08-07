package io.github.x1111101101.glucoseserver.food.dish.model.exchange

import kotlinx.serialization.Serializable

@Serializable
data class FoodExchange(val group: FoodExchangeGroup, val unit: Double) {

    init {
        if(group == FoodExchangeGroup.NONE && unit != 0.0) throw IllegalArgumentException()
    }

    companion object {
        val NONE = FoodExchange(FoodExchangeGroup.NONE, 0.0)
    }

}