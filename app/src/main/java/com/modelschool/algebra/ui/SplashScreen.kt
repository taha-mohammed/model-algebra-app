package com.modelschool.algebra.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    loginViewModel: LoginViewModel,
    navToLogin: () -> Unit,
    navToTopic: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        if (loginViewModel.isLoggedIn.value) {
            navToTopic()
        } else {
            navToLogin()
        }
    }

    // Image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Change the logo
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Logo"
        )
    }
}
