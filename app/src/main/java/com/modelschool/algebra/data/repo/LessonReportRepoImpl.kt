package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.awaitTaskCompletable
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LessonReportRepoImpl @Inject constructor() : LessonReportRepo {

    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getReports(topicId: String, userId: String) = callbackFlow {

        val collection = db.collection(Constants.TOPIC_COLLECTION)
            .document(topicId).collection(Constants.LESSON_REPORT_COLLECTION)
            .whereEqualTo("studentId", userId)
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                if (value != null) {
                    Result.Value(toReports(value))
                } else {
                    Result.Empty
                }
            } else {
                Result.Error(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createReport(topicId: String, report: LessonReport): Result<Unit> {
        var result: Result<Unit> = Result.Idle

        val reference = db.collection(Constants.TOPIC_COLLECTION).document(topicId)
            .collection(Constants.LESSON_REPORT_COLLECTION)

        return try {
            awaitTaskCompletable(reference.add(report))
            Result.Value(Unit)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    private fun toReports(value: QuerySnapshot?): List<LessonReport> {
        val reportList = mutableListOf<LessonReport>()

        value?.forEach { documentSnapshot ->
            reportList.add(
                documentSnapshot.toObject(LessonReport::class.java)
            )
        }
        return reportList
    }
}