package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LessonRepoImpl @Inject constructor() : LessonRepo {

    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getLessons(topicId: String) = callbackFlow {

        val collection = db.collection(Constants.TOPIC_COLLECTION)
            .document(topicId).collection(Constants.LESSON_COLLECTION)
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                Result.Value(toLessons(value))
            } else {
                Result.Error(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }.flowOn(Dispatchers.IO)

    private fun toLessons(value: QuerySnapshot?): List<Lesson> {
        val lessonList = mutableListOf<Lesson>()

        value?.forEach { documentSnapshot ->
            lessonList.add(
                Lesson(
                    documentSnapshot.id,
                    documentSnapshot.getString("title")!!
                )
            )
        }
        return lessonList
    }
}