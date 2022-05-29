package com.example.store.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class CartProductEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val price: Float,
)