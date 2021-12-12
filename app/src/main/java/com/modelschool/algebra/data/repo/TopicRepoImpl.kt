package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TopicRepoImpl @Inject constructor() : TopicRepo {

    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getAllTopics() = callbackFlow {

        val collection = db.collection(Constants.TOPIC_COLLECTION)
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                Result.Value(toTopics(value))
            } else {
                Result.Error(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }.flowOn(Dispatchers.IO)

    private fun toTopics(value: QuerySnapshot?): List<Topic> {
        val topicList = mutableListOf<Topic>()

        value?.forEach { documentSnapshot ->
            topicList.add(
                Topic(
                    documentSnapshot.id,
                    documentSnapshot.getString("title")!!
                )
            )
        }
        return topicList
    }
}