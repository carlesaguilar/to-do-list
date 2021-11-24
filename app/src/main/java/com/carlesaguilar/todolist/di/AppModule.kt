package com.carlesaguilar.todolist.di

import android.app.Application
import androidx.room.Room
import com.carlesaguilar.todolist.feature_task.data.data_source.TaskDatabase
import com.carlesaguilar.todolist.feature_task.data.data_source.TaskDatabase.Companion.DATABASE_NAME
import com.carlesaguilar.todolist.feature_task.data.repository.TaskRepositoryImpl
import com.carlesaguilar.todolist.feature_task.domain.repository.TaskRepository
import com.carlesaguilar.todolist.feature_task.domain.use_case.AddTaskUseCase
import com.carlesaguilar.todolist.feature_task.domain.use_case.DeleteTaskUseCase
import com.carlesaguilar.todolist.feature_task.domain.use_case.GetTasksUseCase
import com.carlesaguilar.todolist.feature_task.domain.use_case.TaskUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.taskDao)
    }

    @Provides
    @Singleton
    fun provideTaskUseCases(repository: TaskRepository): TaskUseCases {
        return TaskUseCases(
            getTasks = GetTasksUseCase(repository),
            addTask = AddTaskUseCase(repository),
            deleteTask = DeleteTaskUseCase(repository)
        )
    }
}