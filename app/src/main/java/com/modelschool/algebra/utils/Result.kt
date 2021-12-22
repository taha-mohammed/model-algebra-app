package com.modelschool.algebra.utils

sealed class Result<out V> {

    data class Value<out V>(val value: V) : Result<V>()
    object Loading: Result<Nothing>()
    object Idle: Result<Nothing>()
    object Empty: Result<Nothing>()
    data class Error(val error: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when(this){
            is Value -> {"Task is Successful"}
            is Error -> {"there is Error : ${this.error.message}"}
            is Loading -> {"Result is on Loading"}
            is Idle -> {"Result is in Idle"}
            is Empty -> {"Result is Empty"}
        }
    }
}