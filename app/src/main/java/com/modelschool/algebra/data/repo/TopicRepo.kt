package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface TopicRepo {

    fun getAllTopics(): Flow<Result<List<Topic>>>
}