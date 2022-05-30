package com.example.store.domain.interactors.product

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.data.local.model.CartProductEntity
import com.example.store.domain.interactors.UseCase
import com.example.store.domain.repository.IProductsRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val productsRepository: IProductsRepository) : UseCase<None, CartProductEntity>() {
    override suspend fun run(params: CartProductEntity): Either<Failure, None> {
        return productsRepository.addToCart(params)
    }
}
