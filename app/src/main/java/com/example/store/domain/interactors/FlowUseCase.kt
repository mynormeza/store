package com.example.store.domain.interactors

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<out Type, in Params>(private val coroutineDispatcher: CoroutineDispatcher) {

    protected abstract fun run(params: Params): Flow<Type>
    operator fun invoke(params: Params): Flow<Type> = run(params)
        .flowOn(coroutineDispatcher)
}
