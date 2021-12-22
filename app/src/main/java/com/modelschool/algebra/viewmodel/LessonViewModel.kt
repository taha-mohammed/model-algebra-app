package com.modelschool.algebra.viewmodel

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    private val _lessonsStateFlow = MutableStateFlow<Result<List<Lesson>>>(Result.Idle)
    private val _reportsStateFlow = MutableStateFlow<Result<List<LessonReport>>>(Result.Idle)

    @ExperimentalCoroutinesApi
    val lessonsStateFlow: StateFlow<Result<List<Lesson>>>
        get() = lessonsWithReports(
            _lessonsStateFlow.value,
            _reportsStateFlow.value
        ) as StateFlow<Result<List<Lesson>>>

    init {
        viewModelScope.launch {
            userId = (authRepo.getCurrent() as Result.Value).value.id

            lessonRepo.getLessons(topicId!!)
                .onStart { _lessonsStateFlow.value = Result.Loading }
                .collect {
                    when (it) {
                        is Result.Value -> {
                            _lessonsStateFlow.value = it
                        }
                        is Result.Empty -> {
                            _lessonsStateFlow.value = Result.Empty
                        }
                    }
                }
            reportRepo.getReports(topicId, userId)
                .onStart { _reportsStateFlow.value = Result.Loading }
                .collect {
                    when (it) {
                        is Result.Value -> {
                            _reportsStateFlow.value = it
                        }

                        is Result.Empty -> {
                            _reportsStateFlow.value = Result.Empty
                        }
                    }
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun lessonsWithReports(
        lessons: Result<List<Lesson>>,
        reports: Result<List<LessonReport>>
    ) = callbackFlow {
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
                        exercises = lesson.exercises,
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
                            exercises = lesson.exercises,
                            status = LessonState.UNLOCKED.name,
                            percent = 0.0
                        )
                    }
                }
            }
        }
        send(lessons)
    }.flowOn(viewModelScope.coroutineContext)
}