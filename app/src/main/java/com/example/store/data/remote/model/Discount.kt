package com.example.store.data.remote.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Discount(
    @SerializedName("codes")
    val codes: MutableList<String> = mutableListOf(),
    @SerializedName("discount_percent")
    val discountPercent: Float = 0f,
    @SerializedName("min_valid_quantity")
    val minValidQuantity: Int = 0,
    @SerializedName("items_to_apply")
    val itemsToApply: Int = 0,
    @SerializedName("reduce_price_by")
    val reducePriceBy: Int = 0,
    @SerializedName("label")
    val label: String = "",
    @SerializedName("message")
    val message: String = "",
) {
    companion object {
        const val MULTI_BUY = "multi_buy_discount"
        const val BULK = "bulk_discount"

        fun fromJson(value: String?): Discount {
            val gson = Gson()
            val type = object : TypeToken<Discount>() {
            }.type

            return if (value.isNullOrEmpty()) {
                Discount()
            } else {
                gson.fromJson(value, type)
            }
        }
    }
}
