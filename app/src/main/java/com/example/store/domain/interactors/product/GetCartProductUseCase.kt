package com.example.store.domain.interactors.product

import arrow.core.None
import com.example.store.data.local.model.CartProductEntity
import com.example.store.di.IoDispatcher
import com.example.store.domain.interactors.FlowUseCase
import com.example.store.domain.repository.IProductsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartProductUseCase @Inject constructor(
    private val repository: IProductsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<List<CartProductEntity>, None>(ioDispatcher) {
    override fun run(params: None): Flow<List<CartProductEntity>> {
        return repository.getCartProducts()
    }
}
