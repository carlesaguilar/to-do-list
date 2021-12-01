package com.carlesaguilar.todolist.feature_task.domain.use_case

import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.repository.TaskRepository

class GetTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        taskId: Int
    ): Task? {
        return repository.getTaskById(taskId)
    }
}