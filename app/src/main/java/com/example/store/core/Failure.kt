package com.example.store.core

sealed class Failure() {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object CacheError : Failure()

    abstract class FeatureFailure() : Failure()
}
