package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepo: StudentRepo): ViewModel() {

    private val _loginState = mutableStateOf<Result<Unit>>(Result.Loading)
    val loginState: State<Result<Unit>>
        get() = _loginState

    fun login(name: String, password: String) = viewModelScope.launch {
         _loginState.value = authRepo.login(name, password)
    }
}