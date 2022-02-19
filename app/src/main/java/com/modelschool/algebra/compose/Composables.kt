package com.modelschool.algebra.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.modelschool.algebra.R


@Composable
fun FullScreenLoading(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@Composable
fun DialogLoading(
    showDialog: Boolean,
    onDismiss: (Boolean) -> Unit
){
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss(false) },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Center,
                modifier = Modifier
                    .size(100.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ShowDialog(
    message: String,
    showDialog: Boolean = true
){
    val openDialog = remember { mutableStateOf(showDialog)  }

    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Ok")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyView(message: String = stringResource(R.string.empty_view_message)){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = message, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
                style = MaterialTheme.typography.body2,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun WidthSpacer(value: Int) = Spacer(modifier = Modifier
    .fillMaxHeight()
    .width(value.dp))

@Composable
fun HeightSpacer(value: Int) = Spacer(modifier = Modifier
    .fillMaxWidth()
    .height(value.dp))

@Composable
fun TitleBox(text: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = Color.Black,
        contentColor = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun ExitAppDialog(
    showDialog: Boolean,
    onDismiss: (Boolean) -> Unit
){
    val activity = (LocalLifecycleOwner.current as ComponentActivity)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss(false)
            },
            title = {
                Text(stringResource(R.string.exit_dialog_title))
            },
            text = {
                Text(stringResource(R.string.exit_dialog_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss(false)
                        activity.finish()
                    }) {
                    Text(stringResource(R.string.exit_dialog_confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss(false)
                    }) {
                    Text(stringResource(R.string.exit_dialog_dismiss))
                }
            }
        )
    }
}