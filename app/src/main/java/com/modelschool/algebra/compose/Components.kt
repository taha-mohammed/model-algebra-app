package com.modelschool.algebra.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
            Text(text = "Model Algebra",fontWeight = FontWeight.Bold)
            Text(text = title,fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.Black))
    }
}

@Composable
fun BackButton(
    onBack: () -> Unit
){
    Row {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        Button(onClick = { onBack() }) {
            Text(text = "Back")
        }
    }
}