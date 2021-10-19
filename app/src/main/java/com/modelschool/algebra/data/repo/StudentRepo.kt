package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.utils.Result

interface StudentRepo {

    fun register(student: Student): Result<Unit>
    fun login(name: String, password: String): Result<Unit>
    fun logout(id: String): Result<Unit>
    fun isSigned():Result<Boolean>
    fun getMe(): Result<Student>
}