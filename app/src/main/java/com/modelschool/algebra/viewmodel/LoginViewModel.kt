package com.modelschool.algebra.viewmodel

import android.util.Log
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

    private val _loginState = mutableStateOf<Result<Unit>>(Result.Idle)
    val loginState: State<Result<Unit>>
        get() = _loginState

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean>
        get() = _isLoggedIn

    init {
        viewModelScope.launch {
            when (val result = authRepo.isLoggedIn()) {
                is Result.Value -> _isLoggedIn.value = result.value
            }
        }
    }

    fun login(name: String, password: String) = viewModelScope.launch {
        val student = Student(name = name, password = password)
        _loginState.value = Result.Loading
        val data = authRepo.isExist(student)
        Log.d("Login", data.toString())
        _loginState.value = when (data) {
            is Result.Value -> {
                if (data.value.password == student.password) {
                    if (data.value.status == StudentState.CONFIRMED.name) {
                        authRepo.login(data.value)
                    } else {
                        Result.Error(Exception("Student is not confirmed"))
                    }
                } else {
                    Result.Error(Exception("Password is wrong"))
                }
            }
            else -> {
                Result.Error(Exception("Student is not Exist"))
            }
        }
    }

}