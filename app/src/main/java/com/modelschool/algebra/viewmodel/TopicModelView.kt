package com.modelschool.algebra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.data.repo.TopicRepo
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TopicModelView(private val topicRepo: TopicRepo): ViewModel() {

    private val _topicsStateFlow = MutableStateFlow<Result<List<Topic>>>(Result.Loading)
    val topicsStateFlow: StateFlow<Result<List<Topic>>>
        get() = _topicsStateFlow

    init {
        viewModelScope.launch {
            topicRepo.getAllTopics().collect {
                when (it) {
                    is Result.Value -> {
                        _topicsStateFlow.value = it
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }
}