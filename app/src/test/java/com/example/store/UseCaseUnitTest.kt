package com.example.store

import arrow.core.Either
import arrow.core.None
import com.example.store.data.ProductsRepository
import com.example.store.domain.interactors.product.GetProductsUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UseCaseUnitTest {
    private lateinit var getProducts: GetProductsUseCase
    @Rule
    @JvmField val injectMocks = InjectMockRule.create(this)

    @MockK
    lateinit var productsRepository: ProductsRepository

    @Before
    fun setUp() {
        getProducts = GetProductsUseCase(productsRepository)
        every { productsRepository.getProducts() } returns Either.Right(emptyList())
    }

    @Test fun `should get data from repository`() {
        runBlocking { getProducts.run(None) }

        verify(exactly = 1) { productsRepository.getProducts() }
    }
}
