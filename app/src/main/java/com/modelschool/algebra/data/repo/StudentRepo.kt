package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface StudentRepo {

    suspend fun register(student: Student): Result<Unit>
    suspend fun login(name: String, password: String): Result<Unit>
    suspend fun logout(id: String): Result<Unit>
    suspend fun isSigned():Result<Boolean>
    fun getMe(): Flow<Result<Student>>
}