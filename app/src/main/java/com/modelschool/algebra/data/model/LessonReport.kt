package com.modelschool.algebra.data.model

data class LessonReport(
    val lessonId: String,
    val studentId: String,
    val studentName: String,
    val percent: Double,
    val status: String,
    val wrongAnswers: List<Answer>
)
