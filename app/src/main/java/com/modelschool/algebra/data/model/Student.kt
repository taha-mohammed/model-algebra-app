package com.modelschool.algebra.data.model

import com.modelschool.algebra.utils.StudentState

data class Student(
    val id: String = "",
    val name: String = "",
    val password: String = "",
    val classNo: Int = 0,
    val level: Int = 1,
    val status: String = StudentState.NOT_CONFIRMED.name
)