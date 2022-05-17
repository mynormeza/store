package com.example.store.di

import com.example.store.data.ProductsRepository
import com.example.store.domain.repository.IProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun bindPlayRepository(productsRepository: ProductsRepository): IProductsRepository
}