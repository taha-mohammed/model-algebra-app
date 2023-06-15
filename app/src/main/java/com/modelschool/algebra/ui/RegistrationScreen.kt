package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var classNo by remember { mutableStateOf(1) }
    var loadingState by remember { mutableStateOf(false) }
    DialogLoading(showDialog = loadingState, onDismiss = { loadingState = it })

    when (val result = registrationViewModel.registerState.value) {
        is Result.Loading -> {
            loadingState = true
        }
        is Result.Error -> {
            loadingState = false
            ShowDialog(message = result.error.message!!)
        }
        is Result.Value -> {
            loadingState = false
            onBack()
        }
        is Result.Idle -> {
            loadingState = false
        }
    }

    RegistrationContent(
        name = name,
        password = password,
        grade = grade,
        classNo = classNo,
        onNameChange = { name = it },
        onPasswordChange = { password = it },
        onGradeSelected = { grade = it },
        onClassNoSelected = { classNo = it }
    ) {
        registrationViewModel.register(
            Student(
                name = name,
                password = password,
                grade = grade,
                classNo = classNo
            )
        )
    }

}

@Composable
fun RegistrationContent(
    name: String,
    password: String,
    grade: String,
    classNo: Int,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onGradeSelected: (String) -> Unit,
    onClassNoSelected: (Int) -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        RegistrationFields(
            name = name,
            password = password,
            grade = grade,
            classNo = classNo,
            onNameChange = onNameChange,
            onPasswordChange = onPasswordChange,
            onGradeSelected = onGradeSelected,
            onClassNoSelected = onClassNoSelected
        )

        HeightSpacer(10)

        SignUpButton(name = name, password = password, grade = grade, onSignUpClick = onSignUpClick)
    }
}

@Composable
fun RegistrationFields(
    name: String,
    password: String,
    grade: String,
    classNo: Int,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onGradeSelected: (String) -> Unit,
    onClassNoSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleBox(text = stringResource(R.string.registration_form), modifier = Modifier.size(200.dp, 35.dp))

        NameField(name = name, onNameChange = onNameChange)

        Row(
            modifier = Modifier.height(45.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                contentColor = Color(0xFF7C771B),
                elevation = 2.dp
            ) {
                GradeField(grade = grade, onGradeSelected = onGradeSelected)
            }

            WidthSpacer(value = 10)

            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                contentColor = Color(0xFF7C771B),
                elevation = 2.dp
            ) {
                ClassNumberField(classNo = classNo, onClassNoSelected = onClassNoSelected)
            }
        }

        PasswordField(password = password, onPasswordChange = onPasswordChange)
    }
}

@Composable
fun SignUpButton(
    name: String,
    password: String,
    grade: String,
    onSignUpClick: () -> Unit
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Button(
        shape = CircleShape,
        border = BorderStroke(3.dp, Color.White),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = Color.White
        ),
        onClick = {
            if (name.isBlank() && password.isBlank() && grade.isBlank()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.empty_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (name.split(Regex("\\s+")).size < 3) {
                Toast.makeText(
                    context,
                    context.getString(R.string.full_name_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onSignUpClick()
                focusManager.clearFocus()
            }
        }
    ) {
        Text(text = stringResource(R.string.signup_button), style = MaterialTheme.typography.h4)
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    AlgebraAppTheme() {
        RegistrationContent(
            name = "",
            password = "",
            grade = "First",
            classNo = 1,
            onNameChange = {},
            onPasswordChange = {},
            onGradeSelected = {},
            onClassNoSelected = {}
        ){

        }
    }
}