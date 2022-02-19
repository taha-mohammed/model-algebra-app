package com.modelschool.algebra.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Answer
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.data.repo.LessonReportRepo
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.LessonState
import com.modelschool.algebra.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepo: LessonReportRepo,
    private val authRepo: StudentRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lessonId: String? = savedStateHandle.get<String>("lesson_id")

    private val _state = mutableStateOf<Result<Unit>>(Result.Idle)
    val state: State<Result<Unit>>
        get() = _state

    fun completeLesson(percent: Double, wrongAnswers: List<Answer>) = viewModelScope.launch {
        _state.value = Result.Loading
        val status = if (percent > 0.5) LessonState.SUCCESS.name else LessonState.FAILED.name
        val student = authRepo.getCurrent() as Result.Value
        val report = LessonReport(
            lessonId = lessonId!!,
            studentId = student.value.id,
            studentName = student.value.name,
            percent = percent,
            status = status,
            wrongAnswers = wrongAnswers
        )
        _state.value = reportRepo.createReport(lessonId, report)
    }
}