package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navToRegister: () -> Unit,
    navToTopic: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loadingState by remember { mutableStateOf(false) }
    DialogLoading(showDialog = loadingState, onDismiss = { loadingState = it })

    when (val result = loginViewModel.loginState.value) {
        is Result.Loading -> {
            loadingState = true
        }
        is Result.Error -> {
            loadingState = false
            ShowDialog(message = result.error.message!!)
        }
        is Result.Value -> {
            loadingState = false
            navToTopic()
        }
        is Result.Idle -> {
            loadingState = false
        }
    }

    LoginContent(
        name = name,
        password = password,
        onNameChange = { name = it },
        onPasswordChange = { password = it },
        onLoginClick = { loginViewModel.login(name, password) },
        navToRegister
    )

}

@Composable
fun LoginContent(
    name: String,
    password: String,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    navToRegister: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LoginFields(
            name = name,
            password = password,
            onNameChange = onNameChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = { onLoginClick() }
        )

        HeightSpacer(value = 40)

        AboutUs()

        HeightSpacer(value = 50)

        Row(
            modifier = Modifier.clickable { navToRegister() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "")
            Text(
                stringResource(R.string.signup_button),
                color = MaterialTheme.colors.primaryVariant,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun LoginFields(
    name: String,
    password: String,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleBox(
            text = stringResource(R.string.login_form),
            modifier = Modifier.size(180.dp, 35.dp)
        )

        NameField(name = name, onNameChange = onNameChange)

        PasswordField(password = password, onPasswordChange = onPasswordChange)

        OutlinedButton(onClick = {
            if (name.isNotBlank() && password.isNotBlank()) {
                onLoginClick()
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.empty_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(stringResource(R.string.login_button), color = MaterialTheme.colors.primaryVariant)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AlgebraAppTheme() {
        LoginContent(
            name = "",
            password = "",
            onNameChange = {},
            onPasswordChange = {},
            onLoginClick = { /*TODO*/ }) {

        }
    }
}
