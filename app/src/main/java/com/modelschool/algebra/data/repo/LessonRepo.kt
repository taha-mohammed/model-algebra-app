package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface LessonRepo {

    fun getLessons(topicId: String): Flow<Result<List<Lesson>>>
}