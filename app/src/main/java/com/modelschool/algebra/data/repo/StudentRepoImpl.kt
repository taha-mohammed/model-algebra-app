package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.AppPrefs
import com.modelschool.algebra.data.awaitTaskCompletable
import com.modelschool.algebra.data.awaitTaskResult
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.data.toRegistrationStudent
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.utils.StudentState
import javax.inject.Inject

class StudentRepoImpl @Inject constructor(private val appPrefs: AppPrefs) : StudentRepo {

    private val db = Firebase.firestore

    override suspend fun register(student: Student): Result<Unit> {

        val reference = db.collection(Constants.STUDENT_COLLECTION)

        return try {
            awaitTaskCompletable(reference.add(student.toRegistrationStudent))
            Result.Value(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    override suspend fun login(student: Student): Result<Unit> {
        appPrefs.setValue(student)
        return Result.Value(Unit)
    }

    override suspend fun logout(id: String): Result<Unit> {
        appPrefs.setValue(null)
        return Result.Value(Unit)
    }

    override suspend fun isLoggedIn(): Result.Value<Boolean> {
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
        val reference = db.collection(Constants.STUDENT_COLLECTION)
            .whereEqualTo("name", student.name)

        return try {
            val task = awaitTaskResult(reference.get())
            Result.Value(toStudent(task).first())
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    override suspend fun getCurrent(): Result<Student> {
        val student = appPrefs.getValue()
        return if (student != null) {
            Result.Value(student)
        } else {
            Result.Error(Exception("You are not logged in"))
        }
    }

    private fun toStudent(value: QuerySnapshot?): List<Student> {
        val studentList = mutableListOf<Student>()

        value?.forEach { documentSnapshot ->
            studentList.add(
                Student(
                    id = documentSnapshot.id,
                    name = documentSnapshot.getString("name")!!,
                    password = documentSnapshot.getString("password")!!,
                    classNo = documentSnapshot.getField<Int>("classNo")!!,
                    level = documentSnapshot.getField<Int>("level")!!,
                    status = documentSnapshot.getString("status")!!
                )
            )
        }
        return studentList
    }
}