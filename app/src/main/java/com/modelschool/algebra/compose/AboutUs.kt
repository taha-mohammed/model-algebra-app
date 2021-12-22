package com.modelschool.algebra.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun AboutUs(){

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Surface(
            modifier = Modifier.size(180.dp, 35.dp),
            shape = CircleShape,
            color = Color.Black,
            contentColor = Color(0xFFBBB000),
            elevation = 8.dp
        ) {
            Text(
                text = "Working Team",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HeightSpacer(value = 4)

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF5F5108), fontWeight = FontWeight.Light)){
                append("Student Idea : ")
            }
            withStyle(style = SpanStyle(color = Color.Black)){
                append("Hussein Mohammed Al-Hamed")
            }
        })
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF5F5108), fontWeight = FontWeight.Light)){
                append("Student Idea : ")
            }
            withStyle(style = SpanStyle(color = Color.Black)){
                append("Hussein Mohammed Al-Hamed")
            }
        })
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF5F5108), fontWeight = FontWeight.Light)){
                append("Student Idea : ")
            }
            withStyle(style = SpanStyle(color = Color.Black)){
                append("Hussein Mohammed Al-Hamed")
            }
        })
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF5F5108), fontWeight = FontWeight.Light)){
                append("Student Idea : ")
            }
            withStyle(style = SpanStyle(color = Color.Black)){
                append("Hussein Mohammed Al-Hamed")
            }
        })
    }

}