package com.modelschool.algebra.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.theme.AlgebraAppTheme

@Composable
fun TopBar(title: String) {
    Column(modifier = Modifier
        .padding(horizontal = 8.dp)
        .fillMaxWidth()
        .height(35.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.app_name),style = MaterialTheme.typography.body2)
            Text(text = title,style = MaterialTheme.typography.body2)
        }
        
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.Black))
        HeightSpacer(value = 5)
    }
}

@Composable
fun BackButton(
    onBack: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp)
                .clickable { onBack() },
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "Back",
            tint = MaterialTheme.colors.surface
        )
        Button(
            onClick = { onBack() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface
            )
        ) {
            Text(text = stringResource(R.string.back_button))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ComponentPreview() {
    AlgebraAppTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            TopBar(title = "Preview")
            BackButton {

            }
        }
    }
}