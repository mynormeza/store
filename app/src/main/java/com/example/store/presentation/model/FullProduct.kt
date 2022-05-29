package com.example.store.presentation.model


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
)