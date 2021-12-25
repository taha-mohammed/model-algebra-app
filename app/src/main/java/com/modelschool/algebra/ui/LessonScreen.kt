package com.modelschool.algebra.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.LessonViewModel

@Composable
fun LessonScreen(
    lessonViewModel: LessonViewModel
) {
    val context = LocalContext.current
    val lessonState = lessonViewModel.lessonsStateFlow.value
    Log.d("Lesson screen", lessonState.toString())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        TopBar(title = "Lessons")
        BackButton {
            {}
        }

        HeightSpacer(value = 10)

        when (lessonState) {
            is Result.Loading -> {
                FullScreenLoading()
            }
            is Result.Empty -> {
                EmptyView("No lessons where found")
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = lessonState.value, key = { item -> item.id }) { item ->
                        LessonsCard(lesson = item, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun LessonsCard(lesson: Lesson, onClick: (String) -> Unit) {
    val context = LocalContext.current

    Card(
        elevation = 4.dp,
        border = BorderStroke(2.dp, Color.Black),
        contentColor = Color.Black,
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

            }
    ) {
        Text(
            text = lesson.title,
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        )
    }
}
