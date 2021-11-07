package com.modelschool.algebra.utils

sealed class Result<out V> {

    data class Value<out V>(val value: V) : Result<V>()
    object Loading: Result<Nothing>()
    data class Error(val error: Exception) : Result<Nothing>()

}