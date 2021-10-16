package com.modelschool.algebra.utils

sealed class Result<out V> {

    data class Value<out V>(val value: V) : Result<V>()
    data class Error(val error: Exception) : Result<Nothing>()

    companion object Factory{
        //higher order functions take functions as parameters or return a function
        //Kotlin has function types name: () -> V
        inline fun <V> build(function: () -> V): Result<V> =
            try {
                Value(function.invoke())
            } catch (e: java.lang.Exception) {
                Error(e)
            }

    }

}