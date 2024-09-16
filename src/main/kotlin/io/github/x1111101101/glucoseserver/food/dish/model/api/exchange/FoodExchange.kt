package io.github.x1111101101.glucoseserver.food.dish.model.api.exchange

import kotlinx.serialization.Serializable

@Serializable
data class FoodExchange(val group: FoodExchangeGroup, val unit: Double) {

    init {
        
    }

    companion object {
        val NONE = FoodExchange(FoodExchangeGroup.NONE, 0.0)
    }

}