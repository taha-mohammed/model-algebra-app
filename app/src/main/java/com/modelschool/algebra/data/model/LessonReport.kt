package com.modelschool.algebra.data.model

typealias wrongAnswer = Map<String, String>

data class LessonReport(
    val userId: String,
    val username: String,
    val percent: Double,
    val status: String,
    val wrongAnswers: List<wrongAnswer>
)
