package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.data.repo.LessonRepo
import com.modelschool.algebra.utils.ContentState
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LessonViewModel(
    val lessonRepo: LessonRepo,
    val topicId: String): ViewModel() {

    val contentState = mutableStateOf(ContentState.LOADING)
    val lessonsStateFlow = MutableStateFlow(emptyList<Lesson>())
    val errorState = mutableStateOf<Exception?>(null)

    init {
        viewModelScope.launch {
            lessonRepo.getLessons(topicId).collect {
                when(it){
                    is Result.Value -> {
                        if (it.value.isEmpty()){
                            contentState.value = ContentState.EMPTY
                        }else{
                            contentState.value = ContentState.DATA
                            lessonsStateFlow.value = it.value
                        }
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