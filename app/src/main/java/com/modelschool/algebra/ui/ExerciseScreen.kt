package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.compose.FullScreenLoading
import com.modelschool.algebra.compose.HeightSpacer
import com.modelschool.algebra.data.model.Answer
import com.modelschool.algebra.data.model.Exercise
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.ExerciseViewModel

@Composable
fun ExerciseScreen(
    exerciseViewModel: ExerciseViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    when (val exerciseState = exerciseViewModel.exercisesStateFlow.value) {
        is Result.Loading -> {
            FullScreenLoading()
        }
        is Result.Empty -> {
            onBack()
        }
        is Result.Error -> {
            Toast.makeText(
                context,
                exerciseState.error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
        is Result.Value -> {
            var index by remember { mutableStateOf(1) }
            QuestionScreen(
                value = exerciseState.value[index - 1]
            ) {
                if (index < exerciseState.value.size) {
                    index++
                }
            }
        }
    }
}

@Composable
fun QuestionScreen(value: Exercise, onNext: (answer: Answer) -> Unit) {
    val options = listOf(value.answer, value.option1, value.option2).shuffled()
    var clicked by remember { mutableStateOf(4) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.Black,
                    border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                    elevation = 2.dp
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = value.question,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5
                    )
                }
            }
            item { HeightSpacer(value = 16) }
            itemsIndexed(items = options) { index, option ->
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = (index != clicked),
                    border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                    onClick = { clicked = index }
                ) {
                    Text(
                        text = option,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
            item { HeightSpacer(value = 16) }
            item {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant,
                        contentColor = Color.White
                    ),
                    onClick = {
                        onNext(
                            Answer(
                                value.id,
                                value.question,
                                options[clicked]
                            )
                        )
                    }) {
                    Text(
                        text = "Accept",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExercisePreview() {
    AlgebraAppTheme() {
        Column {
            QuestionScreen(
                value = Exercise(
                    question = "What is your name?",
                    answer = "Taha",
                    option1 = "Hassan",
                    option2 = "Omer"
                )
            ) {}
        }
    }
}