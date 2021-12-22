package com.modelschool.algebra.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.data.repo.TopicRepo
import com.modelschool.algebra.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val topicRepo: TopicRepo,
    private val studentRepo: StudentRepo
) : ViewModel() {

    private val _topicsStateFlow = MutableStateFlow<Result<List<Topic>>>(Result.Loading)
    val topicsStateFlow: StateFlow<Result<List<Topic>>>
        get() = _topicsStateFlow

    init {
        viewModelScope.launch {
            topicRepo.getAllTopics()
                .collect {
                    Log.d("Topic", it.toString())
                    when (it) {
                        is Result.Value -> {
                            _topicsStateFlow.value = Result.Value(lockTopic(it.value))
                        }
                        is Result.Empty -> {
                            _topicsStateFlow.value = Result.Empty
                        }
                    }
                }
        }
    }

    private suspend fun lockTopic(topics: List<Topic>): List<Topic> {

        return when (val student = studentRepo.getCurrent()) {
            is Result.Value -> {
                val level = student.value.level
                topics.mapIndexed { index, topic ->
                    Topic(
                        topic.id,
                        topic.title,
                        index >= level
                    )
                }
            }
            else -> emptyList()
        }
    }
}