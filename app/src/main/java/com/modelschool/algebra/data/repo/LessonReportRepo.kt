package com.modelschool.algebra.data.repo

import com.modelschool.algebra.data.model.LessonReport
import com.modelschool.algebra.utils.Result
import kotlinx.coroutines.flow.Flow

interface LessonReportRepo {

    fun getReports(topicId: String, userId: String): Flow<Result<List<LessonReport>>>
    suspend fun createReport(topicId: String, report: LessonReport): Result<Unit>
}