package com.example.store.domain.interactors.product

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.di.IoDispatcher
import com.example.store.domain.interactors.FlowUseCase
import com.example.store.domain.interactors.UseCase
import com.example.store.domain.model.Product
import com.example.store.domain.repository.IProductsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCartProductUseCase @Inject constructor(
    private val repository: IProductsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<List<Product>, None>(ioDispatcher) {
    override fun run(params: None): Flow<List<Product>> {
        return repository.getCartProducts()
    }
}
