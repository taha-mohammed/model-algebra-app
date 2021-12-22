package com.modelschool.algebra.data.model

import com.modelschool.algebra.utils.Grades
import com.modelschool.algebra.utils.StudentState

data class RegistrationStudent(
    val name: String = "",
    val password: String = "",
    val classNo: Int = 1,
    val grade: String = Grades.SECOND.name,
    val level: Int = 1,
    val status: String = StudentState.NOT_CONFIRMED.name
)
