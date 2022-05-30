package com.example.store.presentation.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import com.example.store.core.Event
import com.example.store.core.Failure
import com.example.store.data.local.model.CartProductEntity
import com.example.store.data.remote.model.Discount
import com.example.store.domain.interactors.product.ClearCartUseCase
import com.example.store.domain.interactors.product.DeleteCartItemUseCase
import com.example.store.domain.interactors.product.GetCartProductUseCase
import com.example.store.presentation.base.BaseViewModel
import com.example.store.presentation.model.DiscountSummary
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
    private val _discounts = MutableLiveData<DiscountSummary>()
    val discounts get() = _discounts

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun loadCartProducts() {
        viewModelScope.launch {
            getCartProductUseCase(None)
                .catch { handleFailure(Failure.CacheError) }
                .collect { list ->
                    val bulkDiscount = Discount.fromJson(remoteConfig.getString(Discount.BULK))
                    val multiBuy = Discount.fromJson(remoteConfig.getString(Discount.MULTI_BUY))
                    var subTotal = 0f
                    var bDiscounted = 0f
                    val mCount = list.count { p -> multiBuy.codes.contains(p.code) }
                    var timesApplied = 0
                    val discountSummary = DiscountSummary()
                    val bCount = list.count { p -> bulkDiscount.codes.contains(p.code) }

                    list.forEach { p ->
                        if (mCount >= multiBuy.minValidQuantity &&
                            timesApplied < multiBuy.itemsToApply && multiBuy.codes.contains(p.code)
                        ) {
                            bulkDiscount.codes.remove(p.code)
                            timesApplied++
                            p.discountedPrice = p.price - (p.price * multiBuy.discountPercent)
                            bDiscounted += p.price * multiBuy.discountPercent
                        }
                        subTotal += p.price
                    }

                    if (bCount >= bulkDiscount.minValidQuantity) {
                        val reductionFactor = if (bulkDiscount.itemsToApply == 0) {
                            bCount
                        } else {
                            bulkDiscount.itemsToApply
                        }
                        list.filter { p -> bulkDiscount.codes.contains(p.code) }.take(reductionFactor).forEach {
                            it.discountedPrice = it.price - bulkDiscount.reducePriceBy
                        }
                        bDiscounted += bulkDiscount.reducePriceBy * reductionFactor
                    }

                    list.distinctBy { it.code }.forEach { p ->
                        if (bulkDiscount.codes.contains(p.code) && bCount >= bulkDiscount.minValidQuantity) {
                            discountSummary.items += " ${p.name}"
                            discountSummary.discounts += "${bulkDiscount.label} | "
                            discountSummary.count++
                        }
                        if (multiBuy.codes.contains(p.code) && mCount >= multiBuy.minValidQuantity) {
                            discountSummary.items += " ${p.name}"
                            discountSummary.discounts += "${multiBuy.label} | "
                            discountSummary.count++
                        }
                    }

                    _summary.value = PaymentSummary(subTotal, bDiscounted, subTotal - bDiscounted)
                    _products.value = list
                    _discounts.value = discountSummary
                }
        }
    }

    fun deleteFromCart(id: Long) {
        deleteCartItemUseCase(id, viewModelScope) {
            it.fold(::handleFailure) {}
        }
    }

    fun clearCart(isCanceled: Boolean = false) {
        clearCartUseCase(None, viewModelScope) {
            it.fold(::handleFailure) {
                val event = if (isCanceled) {
                    Event.OrderCanceled
                } else {
                    Event.OrderPlaced
                }
                handleEvent(event)
            }
        }
    }
}
