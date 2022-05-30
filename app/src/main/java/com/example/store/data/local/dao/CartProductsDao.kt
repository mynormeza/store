package com.example.store.data.local.dao

import androidx.room.*
import com.example.store.data.local.model.CartProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: CartProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<CartProductEntity>)

    @Query("SELECT * FROM products;")
    fun getAll(): Flow<List<CartProductEntity>>

    @Query("DELETE FROM products WHERE id = :id;")
    fun deleteById(id: Long)

    @Query("DELETE FROM products")
    fun deleteAll()

    @Update
    fun update(product: CartProductEntity)

    @Delete
    fun delete(product: CartProductEntity)
}
