package com.example.store.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.store.core.Event
import com.example.store.core.Failure

abstract class BaseViewModel : ViewModel() {
    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    private val _event: MutableLiveData<Event> = MutableLiveData()
    val event: LiveData<Event> = _event

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure
    }

    protected fun handleEvent(event: Event) {
        _event.value = event
    }
}
