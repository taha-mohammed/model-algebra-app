package com.modelschool.algebra.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.data.repo.LessonRepo
import com.modelschool.algebra.data.repo.LessonReportRepo
import com.modelschool.algebra.data.repo.StudentRepo
import com.modelschool.algebra.utils.LessonState
import com.modelschool.algebra.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val reportRepo: LessonReportRepo,
    private val authRepo: StudentRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val topicId: String? = savedStateHandle.get<String>("topic_id")
    private lateinit var userId: String

    private val _lessonsStateFlow = MutableStateFlow<Result<List<Lesson>>>(Result.Loading)
    private val _reportsStateFlow = MutableStateFlow<Result<List<LessonReport>>>(Result.Loading)

    private val _finalLessonsStateFlow = MutableStateFlow<Result<List<Lesson>>>(Result.Loading)

    val lessonsStateFlow: StateFlow<Result<List<Lesson>>>
        get() = _finalLessonsStateFlow

    init {
        Log.d("Lesson", "initialize")
        viewModelScope.launch {
            lessonRepo.getLessons(topicId!!)
                .collect {
                    Log.d("Lesson", "lesson collect result $it")
                    when (it) {
                        is Result.Value -> {
                            _lessonsStateFlow.value = Result.Value(it.value.reversed())
                            _finalLessonsStateFlow.value = lessonsWithReports(
                                _lessonsStateFlow.value,
                                _reportsStateFlow.value
                            )
                        }
                        is Result.Empty -> {
                            _lessonsStateFlow.value = Result.Empty
                            _finalLessonsStateFlow.value = Result.Empty
                        }
                        is Result.Error -> {
                            _finalLessonsStateFlow.value = it
                        }
                    }
                }
        }
        viewModelScope.launch {
            userId = (authRepo.getCurrent() as Result.Value).value.id
            reportRepo.getReports(topicId!!, userId)
                .collect {
                    Log.d("Lesson", "report collect result $it")
                    when (it) {
                        is Result.Value -> {
                            _reportsStateFlow.value = it
                        }

                        is Result.Empty -> {
                            _reportsStateFlow.value = Result.Empty
                        }
                    }
                    _finalLessonsStateFlow.value = lessonsWithReports(_lessonsStateFlow.value, it)
                }
        }
    }

    private fun lessonsWithReports(
        lessons: Result<List<Lesson>>,
        reports: Result<List<LessonReport>>
    ): Result<List<Lesson>> {
        Log.d("Lesson", "lesson list $lessons")
        Log.d("Lesson", "report list $reports")
        if (lessons is Result.Value) {
            if (reports is Result.Value) {
                var prevStatus = LessonState.SUCCESS.name
                var currStatus: String
                lessons.value.map { lesson ->
                    val report = reports.value.find { it.lessonId == lesson.id }

                    currStatus = report?.status
                        ?: when (prevStatus) {
                            LessonState.SUCCESS.name -> LessonState.UNLOCKED.name
                            else -> LessonState.LOCKED.name
                        }

                    Lesson(
                        id = lesson.id,
                        title = lesson.title,
                        status = currStatus,
                        percent = report?.percent ?: 0.0
                    )
                    prevStatus = currStatus
                }
            } else if (reports is Result.Empty) {
                lessons.value.mapIndexed { index, lesson ->
                    if (index == 0) {
                        Lesson(
                            id = lesson.id,
                            title = lesson.title,
                            status = LessonState.UNLOCKED.name,
                            percent = 0.0
                        )
                    }
                }
            }
        }
        return lessons
    }
}