package com.example.store.domain.interactors.product

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.domain.interactors.UseCase
import com.example.store.domain.repository.IProductsRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(private val productsRepository: IProductsRepository) : UseCase<None, Long>() {
    override suspend fun run(params: Long): Either<Failure, None> {
        return productsRepository.deleteFromCart(params)
    }
}
