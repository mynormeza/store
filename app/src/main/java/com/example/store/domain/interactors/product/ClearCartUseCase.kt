package com.example.store.domain.interactors.product

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.domain.interactors.UseCase
import com.example.store.domain.repository.IProductsRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(private val productsRepository: IProductsRepository) : UseCase<None, None>() {
    override suspend fun run(params: None): Either<Failure, None> {
        return productsRepository.clearCart()
    }
}
