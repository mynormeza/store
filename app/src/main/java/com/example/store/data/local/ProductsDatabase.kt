package com.example.store.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.store.data.local.dao.CartProductsDao
import com.example.store.data.local.model.CartProductEntity

@Database(
    entities = [
        CartProductEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): CartProductsDao
}