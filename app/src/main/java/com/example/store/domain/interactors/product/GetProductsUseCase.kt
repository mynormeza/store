package com.example.store.domain.interactors.product

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.domain.interactors.UseCase
import com.example.store.domain.model.Product
import com.example.store.domain.repository.IProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: IProductsRepository
) : UseCase<List<Product>, None>(){
    override suspend fun run(params: None): Either<Failure, List<Product>> {
        return repository.getProducts()
    }
}