package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.utils.Result

interface StudentRepo {

    suspend fun register(student: Student): Result<Unit>
    suspend fun login(student: Student): Result<Unit>
    suspend fun logout(id: String): Result<Unit>
    suspend fun isLoggedIn(): Result<Boolean>
    suspend fun isConfirmed(student: Student): Result<Boolean>
    suspend fun isExist(student: Student): Result<Student>
    suspend fun getCurrent(): Result<Student>
}