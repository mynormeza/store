package com.example.store.data

import arrow.core.Either
import arrow.core.None
import com.example.store.core.Failure
import com.example.store.core.NetworkValidator
import com.example.store.data.local.ProductsDatabase
import com.example.store.data.local.model.CartProductEntity
import com.example.store.data.remote.response.ProductsResponse
import com.example.store.data.remote.service.ProductsService
import com.example.store.domain.model.Product
import com.example.store.domain.repository.IProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val productsService: ProductsService,
    private val networkValidator: NetworkValidator,
    private val database: ProductsDatabase,
) : IProductsRepository {
    override fun getProducts(): Either<Failure, List<Product>> {
        return when (networkValidator.isNetworkAvailable()) {
            true -> {
                request(
                    productsService.getProducts(),
                    {
                        it.products
                    },
                    ProductsResponse(emptyList()),
                )
            }
            false -> Either.Left(Failure.NetworkConnection)
        }
    }

    override fun getCartProducts(): Flow<List<CartProductEntity>> {
        return database.productsDao().getAll()
    }

    override fun addToCart(product: CartProductEntity): Either<Failure, None> {
        return try {
            database.productsDao().insert(product)
            Either.Right(None)
        } catch (e: Throwable) {
            Either.Left(Failure.CacheError)
        }
    }

    override fun deleteFromCart(id: Long): Either<Failure, None> {
        return try {
            database.productsDao().deleteById(id)
            Either.Right(None)
        } catch (e: Throwable) {
            Either.Left(Failure.CacheError)
        }
    }

    override fun clearCart(): Either<Failure, None> {
        return try {
            database.productsDao().deleteAll()
            Either.Right(None)
        } catch (e: Throwable) {
            Either.Left(Failure.CacheError)
        }
    }

    private fun <T, R> request(
        call: Call<T>,
        transform: (T) -> R,
        default: T
    ): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }
}
