package com.example.store.presentation.model

import com.example.store.data.local.model.CartProductEntity

data class FullProduct(
    val code: String,
    val name: String,
    val price: Float,
    val discountLabel: String = "",
    val discountMessage: String = "",
    val discountPercent: Float = 0f,
    val minValidQuantity: Int = 0,
    val itemsToApply: Int = 0,
    val reducePriceBy: Int = 0,
) {
    fun toEntity() = CartProductEntity(
        code = code,
        name = name,
        price = price,
    )
}
