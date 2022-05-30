package com.example.store.presentation.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import com.example.store.data.local.model.CartProductEntity
import com.example.store.data.remote.model.Discount
import com.example.store.domain.interactors.product.ClearCartUseCase
import com.example.store.domain.interactors.product.DeleteCartItemUseCase
import com.example.store.domain.interactors.product.GetCartProductUseCase
import com.example.store.domain.model.Product
import com.example.store.presentation.base.BaseViewModel
import com.example.store.presentation.model.PaymentSummary
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductUseCase: GetCartProductUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : BaseViewModel() {
    private val _products = MutableLiveData<List<CartProductEntity>>()
    val products get() = _products
    private val _summary = MutableLiveData<PaymentSummary>()
    val summary get() = _summary
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun loadCartProducts() {
        viewModelScope.launch {
            getCartProductUseCase(None)
                .catch { cause -> }
                .collect { list ->

                    val bulkDiscount = Discount.fromJson(remoteConfig.getString(Discount.BULK))
                    val multiBuy = Discount.fromJson(remoteConfig.getString(Discount.MULTI_BUY))
                    var subTotal = 0f
                    var bDiscounted = 0f
                    val mCount = list.count { p -> multiBuy.codes.contains(p.code) }

                    var timesApplied = 0

                    list.forEach { p ->
                        if (mCount >= multiBuy.minValidQuantity && timesApplied < multiBuy.itemsToApply) {
                            bulkDiscount.codes.remove(p.code)
                            timesApplied++
                            bDiscounted += p.price * multiBuy.discountPercent
                        }
                        subTotal += p.price
                    }
                    val bCount = list.count { p -> bulkDiscount.codes.contains(p.code) }

                    if (bCount >= bulkDiscount.minValidQuantity) {
                        bDiscounted += bulkDiscount.reducePriceBy * bCount
                    }

                    _summary.value = PaymentSummary(subTotal, bDiscounted, subTotal - bDiscounted)

                    _products.value = list
                }
        }
    }

    fun deleteFromCart(id: Long) {
        deleteCartItemUseCase(id, viewModelScope) {
            it.fold(::handleFailure) {}
        }
    }

    fun clearCart() {
        clearCartUseCase(None, viewModelScope) {
            it.fold(::handleFailure) {}
        }
    }
}
