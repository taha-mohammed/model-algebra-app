package com.modelschool.algebra.data.repo

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.modelschool.algebra.data.model.Exercise
import com.modelschool.algebra.utils.Constants
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExerciseRepoImpl @Inject constructor(): ExerciseRepo {

    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getExercises(topicId: String, lessonId: String): Flow<Result<List<Exercise>>> =
        callbackFlow {

            var result: Result<List<Exercise>> = Result.Loading
            db.collection(Constants.TOPIC_COLLECTION)
                .document(topicId).collection(Constants.LESSON_COLLECTION)
                .document(lessonId).collection(Constants.EXERCISE_COLLECTION).get()
                .addOnSuccessListener { data ->
                    if (data != null) {
                        result = Result.Value(toExercises(data))
                    } else {
                        Result.Empty
                    }
                }.addOnFailureListener { e ->
                    result = Result.Error(e)
                }
            this.trySend(result).isSuccess

        }.flowOn(Dispatchers.IO)

    private fun toExercises(value: QuerySnapshot?): List<Exercise> {
        val exerciseList = mutableListOf<Exercise>()

        value?.forEach { documentSnapshot ->
            exerciseList.add(
                documentSnapshot.toObject(Exercise::class.java).copy(id = documentSnapshot.id)
            )
        }
        return exerciseList
    }
}