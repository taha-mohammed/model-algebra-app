package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(val authRepo: StudentRepo): ViewModel() {

    private val _registerState = mutableStateOf<Result<Unit>>(Result.Loading)
    val registerState: State<Result<Unit>>
        get() = _registerState

    fun register(student: Student) = viewModelScope.launch {
        val data = authRepo.isExist(student)
        when (data) {
            is Result.Value -> {
                _registerState.value = Result.Error(Exception("Student is already exist"))
            }
            is Result.Error -> {
                _registerState.value = authRepo.register(student)
            }
        }
    }
}