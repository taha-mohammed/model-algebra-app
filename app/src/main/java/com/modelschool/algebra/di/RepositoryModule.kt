package com.modelschool.algebra.di

import com.modelschool.algebra.data.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsTopicRepository(topicRepository: TopicRepoImpl): TopicRepo

    @Singleton
    @Binds
    abstract fun bindsLessonRepository(lessonRepository: LessonRepoImpl): LessonRepo

    @Singleton
    @Binds
    abstract fun bindsExerciseRepository(exerciseRepository: ExerciseRepoImpl): ExerciseRepo

    @Singleton
    @Binds
    abstract fun bindsReportRepository(reportRepository: LessonReportRepoImpl): LessonReportRepo

    @Singleton
    @Binds
    abstract fun bindsStudentRepository(studentRepository: StudentRepoImpl): StudentRepo

}