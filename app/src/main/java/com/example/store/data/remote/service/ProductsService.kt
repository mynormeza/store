package com.example.store.data.remote.service

import com.example.store.data.remote.response.ProductsResponse
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsService @Inject constructor(retrofit: Retrofit) : ProductsApi {
    private val productsApi: ProductsApi by lazy { retrofit.create(ProductsApi::class.java) }

    override fun getProducts(): Call<ProductsResponse> {
        return productsApi.getProducts()
    }
}
