package com.modelschool.algebra.data.model

import com.modelschool.algebra.utils.LessonState

data class Lesson(
    val id: String,
    val title: String,
    val exercises: Int = 10,
    val status: LessonState = LessonState.LOCKED,
    val percent: Double = 0.0
)
