package com.example.store.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.store.domain.model.Product

@Entity(tableName = "products")
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val price: Float,
) {
    fun toProduct() = Product(
        code,
        name,
        price,
    )
}
