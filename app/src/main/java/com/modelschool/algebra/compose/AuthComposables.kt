package com.modelschool.algebra.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.utils.Grades

@Composable
fun NameField(
    name: String,
    onNameChange: (String) -> Unit
){
    TextField(
        value = name,
        modifier = Modifier
            .shadow(2.dp, CircleShape),
        placeholder = { Text(text = stringResource(R.string.name_field_placeholder)) },
        label = { Text(text = stringResource(R.string.name_field_label)) },
        onValueChange = onNameChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedLabelColor = MaterialTheme.colors.primaryVariant
        )
    )
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit
){
    TextField(
        value = password,
        modifier = Modifier
            .shadow(2.dp, CircleShape),
        placeholder = { Text(text = stringResource(R.string.password_field_placeholder)) },
        label = { Text(text = stringResource(R.string.password_field_label)) },
        onValueChange = onPasswordChange,
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedLabelColor = MaterialTheme.colors.primaryVariant
        )
    )
}

@Composable
fun GradeField(
    grade: String,
    onGradeSelected: (String) -> Unit
){
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.grade_field_title) + " : ")
        WidthSpacer(value = 8)

        var expanded by remember { mutableStateOf(false) }
        val items = Grades.values().map {
            stringResource(it.grade_res)
        }

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
                        onGradeSelected(s)
                        expanded = false
                    }) {
                        Text(text = s)
                    }
                }
            }
        }
    }
}

@Composable
fun ClassNumberField(
    classNo: Int,
    onClassNoSelected: (Int) -> Unit
){
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.class_field_title) + " : ")
        val unselectedBtn = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        )
        val selectedBtn = ButtonDefaults.buttonColors(
            MaterialTheme.colors.primaryVariant,
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
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = if (it == classNo) selectedBtn else unselectedBtn,
                    onClick = { onClassNoSelected(it) }
                ) {
                    Text(
                        text = it.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}