package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.modelschool.algebra.compose.*
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

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LoginFields(
            name = name,
            password = password,
            onNameChange = { name = it },
            onPasswordChange = { password = it },
            onLoginClick = { loginViewModel.login(name, password) }
        )

        HeightSpacer(value = 40)

        AboutUs()

        HeightSpacer(value = 20)

        TextButton(onClick = { navToRegister() }) {
            Text("Sign Up")
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

        TitleBox(text = "Login Form", modifier = Modifier.size(180.dp, 35.dp))

        TextField(
            value = name,
            modifier = Modifier
                .shadow(2.dp, CircleShape),
            placeholder = { Text(text = "Enter your name", fontSize = 16.sp) },
            label = { Text(text = "Student Name", fontSize = 16.sp) },
            onValueChange = onNameChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF7C771B),
                focusedLabelColor = Color(0xFF5F5108)
            )
        )

        TextField(
            value = password,
            modifier = Modifier
                .shadow(2.dp, CircleShape),
            placeholder = { Text(text = "Enter your password", fontSize = 16.sp) },
            label = { Text(text = "Password", fontSize = 16.sp) },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0xFF7C771B),
                focusedLabelColor = Color(0xFF5F5108)
            )
        )

        OutlinedButton(onClick = {
            if (name.isNotBlank() && password.isNotBlank()) {
                onLoginClick()
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    "Please enter an email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginFields(name = "", password = "", onNameChange = {}, onPasswordChange = {}) {

    }
}

//@Preview(showBackground = true)
//@Composable
//fun FieldPreview() {
//    AlgebraAppTheme {
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .background(Color(0xFFBBB000))
//                .fillMaxSize()
//        ) {
//
//            TextField(
//                value = "",
//                modifier = Modifier
//                    .height(50.dp)
//                    .shadow(2.dp, CircleShape),
//                label = { Text(text = "Student Name", fontSize = 14.sp) },
//                onValueChange = { },
//                shape = CircleShape,
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent,
//                    unfocusedLabelColor = Color(0xFFBBB000)
//                )
//            )
//
//            HeightSpacer(value = 4)
//
//            TextField(
//                value = "",
//                modifier = Modifier
//                    .shadow(2.dp, CircleShape),
//                label = { Text(text = "Password", fontSize = 14.sp) },
//                onValueChange = { },
//                visualTransformation = PasswordVisualTransformation(),
//                shape = CircleShape,
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent,
//                    unfocusedLabelColor = Color(0xFFBBB000)
//                )
//            )
//
//            HeightSpacer(value = 4)
//
//            OutlinedButton(
//                onClick = { /*TODO*/ },
//                shape = CircleShape
//            ) {
//                Text("Login")
//            }
//
//            TextButton(onClick = { /*TODO*/ }) {
//                Text("Sign Up")
//            }
//
//            Surface(
//                modifier = Modifier.size(180.dp, 35.dp),
//                shape = CircleShape,
//                color = Color.Black,
//                contentColor = Color(0xFFBBB000),
//                elevation = 8.dp
//            ) {
//                Text(
//                    text = "Login Form",
//                    textAlign = TextAlign.Center,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//        }
//    }
//}