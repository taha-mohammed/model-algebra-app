package com.modelschool.algebra.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.compose.*
import com.modelschool.algebra.data.model.Topic
import com.modelschool.algebra.theme.AlgebraAppTheme
import com.modelschool.algebra.utils.Result
import com.modelschool.algebra.viewmodel.TopicViewModel

@Composable
fun TopicScreen(
    topicViewModel: TopicViewModel,
    navToLesson: (topicId: String) -> Unit
) {
    val context = LocalContext.current
    val topicState = topicViewModel.topicsStateFlow.value
    var exitState by remember { mutableStateOf(false) }
    ExitAppDialog(exitState) { exitState = it }
    Log.d("Topic screen", topicState.toString())

    BackHandler {
        exitState = true
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(title = stringResource(R.string.topic_screen_title))

        HeightSpacer(value = 10)

        TitleBox(text = stringResource(R.string.welcome), modifier = Modifier.size(150.dp, 35.dp))

        when (topicState) {
            is Result.Loading -> {
                FullScreenLoading()
            }
            is Result.Empty -> {
                EmptyView(stringResource(R.string.empty_topics))
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
                        TopicCard(topic = item, onClick = { navToLesson(it) })
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
                            context.getString(R.string.locked_topic_message),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    onClick(topic.id)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = topic.title,
                style = MaterialTheme.typography.h3
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


@Preview(showBackground = true)
@Composable
fun TopicScreenPreview() {
    AlgebraAppTheme() {
        Column {
            TopicCard(
                topic = Topic(
                    "",
                    "New Topic",
                    false
                ), onClick = {})
            TopicCard(
                topic = Topic(
                    "",
                    "Locked Topic",
                    true
                ), onClick = {})
        }
    }
}