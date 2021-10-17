package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.data.repo.TopicRepo
import com.modelschool.algebra.utils.ContentState
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TopicModelView(val topicRepo: TopicRepo): ViewModel() {

    val contentState = mutableStateOf(ContentState.LOADING)
    val topicsStateFlow = MutableStateFlow(emptyList<Topic>())
    val errorState = mutableStateOf<Exception?>(null)

    init {
        viewModelScope.launch {
            topicRepo.getAllTopics().collect {
                when(it){
                    is Result.Value -> {
                        contentState.value = ContentState.DATA
                        topicsStateFlow.value = it.value
                    }
                    is Result.Error -> {
                        contentState.value = ContentState.ERROR
                        errorState.value = it.error
                    }
                }
            }
        }
    }
}