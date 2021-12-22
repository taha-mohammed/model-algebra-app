package com.modelschool.algebra.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.TopicViewModel

@Composable
fun TopicScreen(
    topicViewModel: TopicViewModel,
    navToLesson: (topicId: String) -> Unit
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        TopBar(title = "Topics")

        HeightSpacer(value = 10)

        TitleBox(text = "Welcome", modifier = Modifier.size(150.dp, 35.dp))

        when (val topicState = topicViewModel.topicsStateFlow.value) {
            is Result.Loading -> {
                FullScreenLoading()
            }
            is Result.Empty -> {
                EmptyView("No topics where found")
            }
            is Result.Error -> {
                Toast.makeText(
                    context,
                    topicState.error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Result.Value -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = topicState.value, key = { item -> item.id }) { item ->
                        TopicCard(topic = item, onClick = {navToLesson(it)})
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(topic: Topic, onClick: (topicId: String) -> Unit) {
    val context = LocalContext.current

    Card(
        elevation = 4.dp,
        border = BorderStroke(2.dp, Color.Black),
        contentColor = Color.Black,
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .size(150.dp)
            .padding(4.dp)
            .shadow(1.dp, CircleShape)
            .clickable {
                if (topic.locked) {
                    Toast
                        .makeText(
                            context,
                            "Complete previous topics",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    onClick(topic.id)
                }
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AutoSizeText(
                text = topic.title,
                textStyle = TextStyle(fontWeight = FontWeight.ExtraBold)
            )
            if (topic.locked) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}