package com.modelschool.algebra.data.model

import com.modelschool.algebra.utils.LessonState

typealias wrongAnswer = Map<String, String>

data class LessonReport(
    val userId: String,
    val username: String,
    val percent: Double,
    val status: LessonState,
    val wrongAnswers: List<wrongAnswer>
)
