package com.ciejaycoding.letsmeat.utils

sealed class UiState<out T> {

    object Loading : UiState<Nothing>()


    data class Success<out T>(val data : T) : UiState<T>()

    data class Failed(val message : String) : UiState<Nothing>()
}
