package com.carlesaguilar.todolist.feature_task.data.repository

import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskRepository : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun getTasks(): Flow<List<Task>> {
        return flow { emit(tasks) }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    override suspend fun insertTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun deleteTask(task: Task) {
        tasks.remove(task)
    }
}