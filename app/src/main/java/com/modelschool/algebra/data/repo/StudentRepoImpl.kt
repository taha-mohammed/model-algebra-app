package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.AppPrefs
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.data.model.UserPreferences
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.utils.StudentState
import javax.inject.Inject

class StudentRepoImpl @Inject constructor(private val appPrefs: AppPrefs) : StudentRepo {

    private val db = Firebase.firestore

    override suspend fun register(student: Student): Result<Unit> {
        val result = db.collection(Constants.STUDENT_COLLECTION).add(student)
        return if (result.isSuccessful) Result.Value(Unit) else Result.Error(result.exception!!)
    }

    override suspend fun login(student: Student): Result<Unit> {
        appPrefs.setValue(
            UserPreferences(
                student.id,
                student.name,
                student.classNo,
                student.level
            )
        )
        return Result.Value(Unit)
    }

    override suspend fun logout(id: String): Result<Unit> {
        appPrefs.setValue(null)
        return Result.Value(Unit)
    }

    override suspend fun isSigned(): Result<Boolean> {
        return Result.Value(appPrefs.getValue() != null)
    }

    override suspend fun isConfirmed(student: Student): Result<Boolean> {
        val result = db.collection(Constants.STUDENT_COLLECTION)
            .whereEqualTo("name", student.name)
            .whereEqualTo("password", student.password)
            .get()
        return if (result.result!!.any { it.get("status")!! == StudentState.CONFIRMED.name })
            Result.Value(true) else Result.Value(false)
    }

    override suspend fun isExist(student: Student): Result<Student> {
        val result = db.collection(Constants.STUDENT_COLLECTION)
            .whereEqualTo("name", student.name)
            .whereEqualTo("password", student.password)
            .get()
        return if (result.isSuccessful) Result.Value(toStudent(result.result).first()) else Result.Error(result.exception!!)
    }

    override suspend fun getCurrent(): Result<Student> {
        val student = appPrefs.getValue()!!
        return Result.Value(
            Student(
                id = student.id,
                name = student.name,
                classNo = student.classNo,
                level = student.level
            )
        )
    }

    private fun toStudent(value: QuerySnapshot?): List<Student> {
        val studentList = mutableListOf<Student>()

        value?.forEach { documentSnapshot ->
            studentList.add(
                Student(
                    id = documentSnapshot.id,
                    name = documentSnapshot.getString("name")!!,
                    classNo = documentSnapshot.getField<Int>("classNo")!!,
                    level = documentSnapshot.getField<Int>("level")!!,
                    status = documentSnapshot.getString("status")!!
                )
            )
        }
        return studentList
    }
}