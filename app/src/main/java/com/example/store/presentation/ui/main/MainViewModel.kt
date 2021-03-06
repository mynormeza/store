package com.example.store.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import com.example.store.R
import com.example.store.core.Event
import com.example.store.data.remote.model.Discount
import com.example.store.domain.interactors.product.AddToCartUseCase
import com.example.store.domain.interactors.product.GetProductsUseCase
import com.example.store.presentation.base.BaseViewModel
import com.example.store.presentation.model.FullProduct
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : BaseViewModel() {
    private val _products = MutableLiveData<List<FullProduct>>()
    val products get() = _products
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun loadProducts() {
        getProductsUseCase(None, viewModelScope) {
            it.fold(::handleFailure) { list ->
                remoteConfig.fetch().addOnCompleteListener {
                    val discounts = mutableListOf<Discount>()
                    discounts.add(Discount.fromJson(remoteConfig.getString(Discount.BULK)))
                    discounts.add(Discount.fromJson(remoteConfig.getString(Discount.MULTI_BUY)))

                    _products.value = list.map { p ->
                        val dis = discounts.firstOrNull() { d -> d.codes.contains(p.code) }
                        FullProduct(p.code, p.name, p.price, dis?.label ?: "", dis?.message ?: "")
                    }
                }

            }
        }
    }

    fun addToCart(product: FullProduct) {
        addToCartUseCase(product.toEntity(), viewModelScope) {
            it.fold(::handleFailure) {
                handleEvent(Event.Message(R.string.item_added_to_cart, product.name))
            }
        }
    }
}
