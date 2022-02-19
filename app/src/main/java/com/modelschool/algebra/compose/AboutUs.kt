package com.modelschool.algebra.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.modelschool.algebra.R
import com.modelschool.algebra.theme.AlgebraAppTheme

@Composable
fun AboutUs() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TitleBox(text = stringResource(R.string.about_us_title), modifier = Modifier.size(180.dp, 35.dp))

        HeightSpacer(value = 4)

        Person(role = "Student Idea", name = "Hussein Mohammed Al-Hamed")
        Person(role = "Student Idea", name = "Hussein Mohammed Al-Hamed")
        Person(role = "Student Idea", name = "Hussein Mohammed Al-Hamed")
        Person(role = "Student Idea", name = "Hussein Mohammed Al-Hamed")
        Person(role = "Student Idea", name = "Hussein Mohammed Al-Hamed")
    }

}

@Composable
fun Person(role: String, name: String){
    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primaryVariant, fontWeight = FontWeight.Light)) {
            append("$role : ")
        }
        withStyle(style = SpanStyle(color = Color.Black)) {
            append(name)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    AlgebraAppTheme() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AboutUs()
        }
    }
}