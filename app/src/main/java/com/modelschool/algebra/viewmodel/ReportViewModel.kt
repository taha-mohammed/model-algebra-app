package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.data.model.wrongAnswer
import com.modelschool.algebra.data.repo.LessonReportRepo
import com.modelschool.algebra.utils.LessonState
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepo: LessonReportRepo,
    private val lessonId: String): ViewModel() {

    private val _state = mutableStateOf<Result<Unit>>(Result.Loading)
    val state: State<Result<Unit>>
        get() = _state

    fun completeLesson(percent: Double, wrongAnswers: List<wrongAnswer>) = viewModelScope.launch {
        val status = if (percent > 0.5) LessonState.SUCCESS.name else LessonState.FAILED.name
        val report = LessonReport(
            studentId = "",
            studentName = "",
            percent = percent,
            status = status,
            wrongAnswers = wrongAnswers
        )
        _state.value = reportRepo.createReport(lessonId, report)
    }
}