package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface LessonReportRepo {

    fun getMyReports(lessonId: String): Flow<Result<List<LessonReport>>>
    suspend fun createReport(lessonId: String, report: LessonReport): Result<Unit>
}