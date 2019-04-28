package com.kotlin.helpers.models

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    class Success<out T>(val data: T): ViewState<T>()
    class Error(val errorMessage: String): ViewState<Nothing>()
}

sealed class CompletableViewState {
    object Loading : CompletableViewState()
    object Completed : CompletableViewState()
    class Error(val errorMessage: String): CompletableViewState()
}