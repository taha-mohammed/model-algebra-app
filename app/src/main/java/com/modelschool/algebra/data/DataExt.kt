package com.modelschool.algebra.data

import com.google.android.gms.tasks.Task
import com.modelschool.algebra.data.model.RegistrationStudent
import com.modelschool.algebra.data.model.Student
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}
//Wraps Firebase/GMS calls
suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

internal val Student.toRegistrationStudent: RegistrationStudent
    get() = RegistrationStudent(
        this.name,
        this.password,
        this.classNo,
        this.grade,
        this.level,
        this.status
    )

internal val RegistrationStudent.toStudent: Student
    get() = Student(
        name = this.name,
        password = this.password,
        classNo = this.classNo,
        grade = this.grade,
        level = this.level,
        status = this.status
    )