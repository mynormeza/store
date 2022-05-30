package com.example.store.domain.repository

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.data.local.model.CartProductEntity
import com.example.store.domain.model.Product

interface IProductsRepository {
    fun getProducts(): Either<Failure, List<Product>>
    fun addToCart(product: CartProductEntity): Either<Failure, None>
}
