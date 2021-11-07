package com.modelschool.algebra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.data.repo.LessonRepo
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LessonViewModel(
    private val lessonRepo: LessonRepo,
    private val topicId: String): ViewModel() {

    private val _lessonsStateFlow = MutableStateFlow<Result<List<Lesson>>>(Result.Loading)
    val lessonsStateFlow: StateFlow<Result<List<Lesson>>>
        get() = _lessonsStateFlow

    init {
        viewModelScope.launch {
            lessonRepo.getLessons(topicId).collect {
                when (it) {
                    is Result.Value -> {
                        _lessonsStateFlow.value = it
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }
}