package com.example.store.data.remote.service

import com.example.store.data.remote.response.ProductsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProductsApi {
    @GET("/palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json")
    fun getProducts(): Call<ProductsResponse>
}
