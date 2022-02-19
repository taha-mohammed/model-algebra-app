package com.modelschool.algebra.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.LessonState
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.LessonViewModel

@Composable
fun LessonScreen(
    lessonViewModel: LessonViewModel,
    onBack: () -> Unit,
    navToExercise: (String) -> Unit
) {
    val context = LocalContext.current
    val lessonState = lessonViewModel.lessonsStateFlow.value
    Log.d("Lesson screen", lessonState.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(title = stringResource(R.string.lesson_screen_title))
        BackButton { onBack() }

        HeightSpacer(value = 10)

        when (lessonState) {
            is Result.Loading -> {
                FullScreenLoading()
            }
            is Result.Empty -> {
                EmptyView(stringResource(R.string.empty_lessons))
            }
            is Result.Error -> {
                Toast.makeText(
                    context,
                    lessonState.error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Result.Value -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = lessonState.value, key = { item -> item.id }) { item ->
                        LessonCard(lesson = item, onClick = navToExercise)
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson, onClick: (String) -> Unit) {

    Card(
        elevation = 4.dp,
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(lesson.id)
            }
    ) {
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.h3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    AlgebraAppTheme() {
        Column {
            LessonCard(
                lesson = Lesson(
                    "",
                    "New Lesson",
                    LessonState.UNLOCKED.name
                ), onClick = {})
            LessonCard(
                lesson = Lesson(
                    "",
                    "Locked Lesson"
                ), onClick = {})
        }
    }
}