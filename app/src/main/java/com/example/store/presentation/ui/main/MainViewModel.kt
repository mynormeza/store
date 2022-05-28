package com.example.store.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.None
import com.example.store.domain.interactors.product.GetProductsUseCase
import com.example.store.domain.model.Product
import com.example.store.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel() {
    private val _post = MutableLiveData<List<Product>>()
    val post get() = _post

    fun loadProducts() {
        getProductsUseCase(None, viewModelScope) {
            it.fold(::handleFailure) { list ->
                _post.value = list
            }
        }
    }
}