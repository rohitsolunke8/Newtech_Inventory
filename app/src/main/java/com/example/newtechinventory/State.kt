package com.example.newtechinventory

sealed class State<out T> {
    data object Loading: State<Nothing>()
    data class Success<out T>(val data: T): State<@UnsafeVariance T>()
    data class Error(val message: String): State<Nothing>()
}