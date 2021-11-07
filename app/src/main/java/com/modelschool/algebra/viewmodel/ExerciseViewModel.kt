package com.modelschool.algebra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Exercise
import com.modelschool.algebra.data.repo.ExerciseRepo
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepo: ExerciseRepo,
    private val lessonId: String): ViewModel() {

    private val _exercisesStateFlow = MutableStateFlow<Result<List<Exercise>>>(Result.Loading)
    val exercisesStateFlow: StateFlow<Result<List<Exercise>>>
        get() = _exercisesStateFlow

    init {
        viewModelScope.launch {
            exerciseRepo.getExercises(lessonId).collect {
                when (it) {
                    is Result.Value -> {
                        _exercisesStateFlow.value = it
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }
}