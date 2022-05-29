package com.example.store.domain.repository

import arrow.core.Either
import com.example.store.core.Failure
import com.example.store.domain.model.Product

interface IProductsRepository {
    fun getProducts(): Either<Failure, List<Product>>
}
