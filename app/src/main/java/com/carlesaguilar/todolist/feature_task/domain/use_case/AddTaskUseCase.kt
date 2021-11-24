package com.carlesaguilar.todolist.feature_task.domain.use_case

import com.carlesaguilar.todolist.feature_task.domain.model.InvalidTaskException
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository
) {
    @Throws(InvalidTaskException::class)
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            throw InvalidTaskException("The title can't be empty.")
        }
        repository.insertTask(task)
    }
}