package com.example.store.data.local.dao

import androidx.room.*
import com.example.store.data.local.model.CartProductEntity

@Dao
interface CartProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: CartProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<CartProductEntity>)

    @Query("SELECT * FROM products;")
    fun getAll(): List<CartProductEntity>
    
    @Update
    fun update(product: CartProductEntity)

    @Delete
    fun delete(product: CartProductEntity)
}