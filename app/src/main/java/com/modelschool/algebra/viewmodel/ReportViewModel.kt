package com.modelschool.algebra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.data.model.wrongAnswer
import com.modelschool.algebra.data.repo.LessonReportRepo
import com.modelschool.algebra.utils.LessonState
import kotlinx.coroutines.launch

class ReportViewModel(
    val reportRepo: LessonReportRepo,
    val lessonId: String): ViewModel() {

    fun createReport(percent: Double, wrongAnswers: List<wrongAnswer>) = viewModelScope.launch {
        val status = if (percent > 0.5) LessonState.SUCCESS.name else LessonState.FAILED.name
        val report = LessonReport(
            studentId = "",
            studentName = "",
            percent = percent,
            status = status,
            wrongAnswers = wrongAnswers
        )
        reportRepo.createReport(lessonId, report)
    }
}