package com.modelschool.algebra.di

import android.content.Context
import com.modelschool.algebra.data.AppPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {
    @Provides
    @Singleton
    fun provideAppPrefs(@ApplicationContext context: Context) = AppPrefs(context)
}