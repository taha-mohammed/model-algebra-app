package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.utils.StudentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepo: StudentRepo) : ViewModel() {

    private val _loginState = mutableStateOf<Result<Unit>>(Result.Loading)
    val loginState: State<Result<Unit>>
        get() = _loginState

    fun login(student: Student) = viewModelScope.launch {
        val data = authRepo.isExist(student)
        _loginState.value = when (data) {
            is Result.Value -> {
                if (data.value.password == student.password){
                    if (data.value.status == StudentState.CONFIRMED.name) {
                        authRepo.login(student)
                    } else {
                        Result.Error(Exception("Student is not confirmed"))
                    }
                }else{
                    Result.Error(Exception("Password is wrong"))
                }
            }
            else -> Result.Error(Exception("Student is not Exist"))
        }
    }
}