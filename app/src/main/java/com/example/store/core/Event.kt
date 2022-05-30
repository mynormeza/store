package com.example.store.core

sealed class Event {
    object OrderPlaced : Event()
    object OrderCanceled : Event()
    data class Message(val stringRes: Int = 0, val message: String = "") : Event()
}
