package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Exercise
import com.modelschool.algebra.data.repo.ExerciseRepo
import com.modelschool.algebra.utils.ContentState
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExerciseViewModel(
    val exerciseRepo: ExerciseRepo,
    val lessonId: String): ViewModel() {

    val contentState = mutableStateOf(ContentState.LOADING)
    val exercisesStateFlow = MutableStateFlow(emptyList<Exercise>())
    val errorState = mutableStateOf<Exception?>(null)

    init {
        viewModelScope.launch {
            exerciseRepo.getExercises(lessonId).collect {
                when(it){
                    is Result.Value -> {
                        if (it.value.isEmpty()){
                            contentState.value = ContentState.EMPTY
                        }else{
                            contentState.value = ContentState.DATA
                            exercisesStateFlow.value = it.value
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