package com.modelschool.algebra.data.model

typealias wrongAnswer = Map<String, String>

data class LessonReport(
    val lessonId: String,
    val studentId: String,
    val studentName: String,
    val percent: Double,
    val status: String,
    val wrongAnswers: List<wrongAnswer>
)
