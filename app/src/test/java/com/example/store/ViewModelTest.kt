package com.example.store

import android.app.Application
import android.os.Build
import arrow.core.Either
import com.example.store.domain.interactors.product.GetProductsUseCase
import com.example.store.domain.model.Product
import com.example.store.presentation.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = ViewModelTest.ApplicationStub::class,
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.O_MR1])
class ViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    @Rule
    @JvmField val injectMocks = InjectMockRule.create(this)

    @MockK
    private lateinit var getProducts: GetProductsUseCase

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(getProducts)
    }

    @Test
    fun `loading products should update live data`() {
        val products = listOf(Product("1", "Cup", 4f), Product("2", "Shirt", 6f))
        coEvery { getProducts.run(any()) } returns Either.Right(products)

        mainViewModel.products.observeForever {
            it?.size shouldBeEqualTo 2
            it[0].code shouldBeEqualTo "1"
            it[0].name shouldBeEqualTo "Cup"
            it[1].code shouldBeEqualTo "2"
            it[1].name shouldBeEqualTo "Shirt"
        }

        runBlocking { mainViewModel.loadProducts() }
    }

    internal class ApplicationStub : Application()
}