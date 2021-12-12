package com.modelschool.algebra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modelschool.algebra.data.model.Lesson
import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.data.repo.LessonRepo
import com.modelschool.algebra.data.repo.LessonReportRepo
import com.modelschool.algebra.utils.LessonState
import com.modelschool.algebra.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val reportRepo: LessonReportRepo,
    private val userId: String,
    private val topicId: String
) : ViewModel() {

    private val _lessonsStateFlow = MutableStateFlow<Result<List<Lesson>>>(Result.Loading)
    private val _reportsStateFlow = MutableStateFlow<Result<List<LessonReport>>>(Result.Loading)

    @ExperimentalCoroutinesApi
    val lessonsStateFlow: StateFlow<Result<List<Lesson>>>
        get() = lessonsWithReports(
            _lessonsStateFlow.value,
            (_reportsStateFlow.value as Result.Value).value
        ) as StateFlow<Result<List<Lesson>>>

    init {
        viewModelScope.launch {
            lessonRepo.getLessons(topicId).collect {
                when (it) {
                    is Result.Value -> {
                        _lessonsStateFlow.value = it
                    }
                    is Result.Error -> {
                    }
                }
            }
            reportRepo.getReports(topicId, userId).collect {
                when (it) {
                    is Result.Value -> {
                        _reportsStateFlow.value = it
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun lessonsWithReports(
        lessons: Result<List<Lesson>>,
        reports: List<LessonReport>
    ) = callbackFlow {
        when (lessons) {
            is Result.Value -> {
                var prevStatus = LessonState.SUCCESS.name
                var currStatus: String
                lessons.value.map { lesson ->
                    val report = reports.find { it.lessonId == lesson.id }

                    currStatus = report?.status
                        ?: when (prevStatus) {
                            LessonState.SUCCESS.name -> LessonState.UNLOCKED.name
                            else -> LessonState.LOCKED.name
                        }

                    Lesson(
                        id = lesson.id,
                        title = lesson.title,
                        exercises = lesson.exercises,
                        status = currStatus,
                        percent = report?.percent ?: 0.0
                    )
                    prevStatus = currStatus
                }
            }
            else -> {
            }
        }
        send(lessons)
    }.flowOn(viewModelScope.coroutineContext)
}