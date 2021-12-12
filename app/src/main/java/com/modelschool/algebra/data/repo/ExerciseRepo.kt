package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Exercise
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface ExerciseRepo {

    fun getExercises(topicId: String, lessonId: String): Flow<Result<List<Exercise>>>
}