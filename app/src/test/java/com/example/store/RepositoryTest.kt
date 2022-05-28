package com.example.store

import arrow.core.Either
import com.example.store.core.Failure
import com.example.store.core.NetworkValidator
import com.example.store.data.ProductsRepository
import com.example.store.data.remote.response.ProductsResponse
import com.example.store.data.remote.service.ProductsService
import com.example.store.domain.model.Product
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class RepositoryTest {
    private lateinit var networkRepository: ProductsRepository
    @Rule
    @JvmField val injectMocks = InjectMockRule.create(this)

    @MockK
    private lateinit var networkValidator: NetworkValidator
    @MockK private lateinit var service: ProductsService

    @MockK private lateinit var productsCall: Call<ProductsResponse>
    @MockK private lateinit var productsResponse: Response<ProductsResponse>


    @Before
    fun setUp() {
        networkRepository = ProductsRepository(service,networkValidator)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkValidator.isNetworkAvailable() } returns true
        every { productsResponse.body() } returns null
        every { productsResponse.isSuccessful } returns true
        every { productsCall.execute() } returns productsResponse
        every { service.getProducts() } returns productsCall

        val products = networkRepository.getProducts()

        products shouldBeEqualTo Either.Right(emptyList())
        verify(exactly = 1) { service.getProducts()}
    }

    @Test fun `should get product list from service`() {
        every { networkValidator.isNetworkAvailable() } returns true
        every { productsResponse.body() } returns ProductsResponse(listOf(Product("1", "Cup", 8f)))
        every { productsResponse.isSuccessful } returns true
        every { productsCall.execute() } returns productsResponse
        every { service.getProducts() } returns productsCall

        val products = networkRepository.getProducts()

        products shouldBeEqualTo Either.Right(listOf(Product("1", "Cup", 8f)))
        verify(exactly = 1) { service.getProducts() }
    }

    @Test fun `products service should return network failure when no connection`() {
        every { networkValidator.isNetworkAvailable() } returns false

        val products = networkRepository.getProducts()

        products shouldBeInstanceOf Either::class.java
        products.isLeft() shouldBeEqualTo true
        products.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verify { service wasNot Called }
    }

    @Test fun `products service should return server error if no successful response`() {
        every { networkValidator.isNetworkAvailable() } returns true
        every { productsResponse.isSuccessful } returns false
        every { productsCall.execute() } returns productsResponse
        every { service.getProducts() } returns productsCall

        val products = networkRepository.getProducts()

        products shouldBeInstanceOf Either::class.java
        products.isLeft() shouldBeEqualTo true
        products.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test fun `products request should catch exceptions`() {
        every { networkValidator.isNetworkAvailable() } returns true
        every { productsCall.execute() } returns productsResponse
        every { service.getProducts() } returns productsCall

        val products = networkRepository.getProducts()

        products shouldBeInstanceOf Either::class.java
        products.isLeft() shouldBeEqualTo true
        products.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

}