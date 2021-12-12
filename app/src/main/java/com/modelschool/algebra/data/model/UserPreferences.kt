package com.modelschool.algebra.data.model

import java.io.Serializable

data class UserPreferences(
    val id: String,
    val name: String,
    val classNo: Int,
    val level: Int
):Serializable