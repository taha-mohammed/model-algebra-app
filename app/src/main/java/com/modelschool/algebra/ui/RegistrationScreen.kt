package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Student
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.Grades
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf(Grades.SECOND.grade) }
    var classNo by remember { mutableStateOf(1) }
    val focusManager = LocalFocusManager.current
    var loadingState by remember { mutableStateOf(false) }
    val context = LocalContext.current
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

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        RegistrationFields(
            name = name,
            password = password,
            grade = grade,
            classNo = classNo,
            onNameChange = { name = it },
            onPasswordChange = { password = it },
            onGradeSelected = { grade = it },
            onClassNoSelected = { classNo = it }
        )

        HeightSpacer(value = 40)

        AboutUs()

        HeightSpacer(value = 20)

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
                        "Please enter an email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if(name.split(Regex("\\s+")).size < 3){
                    Toast.makeText(
                        context,
                        "Please write your full name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else{
                    val student = Student(
                        name = name,
                        password = password,
                        grade = grade,
                        classNo = classNo
                    )
                    registrationViewModel.register(student)
                    focusManager.clearFocus()
                }
            }
        ) {
            Text(text = "Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
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
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleBox(text = "Registration Form", modifier = Modifier.size(200.dp, 35.dp))

        TextField(
            value = name,
            modifier = Modifier
                .shadow(2.dp, CircleShape),
            placeholder = { Text(text = "Enter your name") },
            label = { Text(text = "Student Name") },
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

        Row(
            modifier = Modifier.height(45.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = CircleShape,
                color = Color.White,
                contentColor = Color(0xFF7C771B),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Grade : ")
                    WidthSpacer(value = 8)

                    var expanded by remember { mutableStateOf(false) }
                    val items = Grades.values()

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            grade,
                            modifier = Modifier
                                .clickable(onClick = { expanded = !expanded })
                                .padding(end = 8.dp)
                        )
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            items.forEach { s ->
                                DropdownMenuItem(onClick = {
                                    onGradeSelected(s.grade)
                                    expanded = false
                                }) {
                                    Text(text = s.grade)
                                }
                            }
                        }
                    }
                }
            }

            WidthSpacer(value = 10)

            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = CircleShape,
                color = Color.White,
                contentColor = Color(0xFF7C771B),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Class : ")
                    val unselectedBtn = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xffBEAC03),
                        contentColor = Color.White
                    )
                    val selectedBtn = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF7C771B),
                        contentColor = Color.White
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (1..3).forEach {
                            Button(
                                modifier = Modifier
                                    .size(30.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                border = BorderStroke(1.dp, Color.Black),
                                colors = if (it == classNo) selectedBtn else unselectedBtn,
                                onClick = { onClassNoSelected(it) }
                            ) {
                                Text(
                                    text = it.toString(),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        TextField(
            value = password,
            modifier = Modifier
                .shadow(2.dp, CircleShape),
            placeholder = { Text(text = "Enter your password") },
            label = { Text(text = "Password") },
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
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview(){
    AlgebraAppTheme() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize()
                .padding(8.dp)
        ){
            RegistrationFields(
                name = "",
                password = "",
                grade = "First",
                classNo = 1,
                onNameChange = {},
                onPasswordChange = {},
                onGradeSelected = {},
                onClassNoSelected = {}
            )
        }
    }
}