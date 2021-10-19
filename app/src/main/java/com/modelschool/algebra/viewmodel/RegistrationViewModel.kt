package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.ContentState
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.launch

class RegistrationViewModel(val authRepo: StudentRepo): ViewModel() {

    val errorState = mutableStateOf<Exception?>(null)
    val contentState = mutableStateOf(ContentState.LOADING)

    fun register(student: Student) = viewModelScope.launch {
        when (val result = authRepo.register(student)) {
            is Result.Value -> {
                contentState.value = ContentState.DATA
            }
            is Result.Error -> {
                contentState.value = ContentState.ERROR
                errorState.value = result.error
            }
        }
    }
}