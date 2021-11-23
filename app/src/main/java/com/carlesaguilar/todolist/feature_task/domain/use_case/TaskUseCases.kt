package com.carlesaguilar.todolist.feature_task.domain.use_case

data class TaskUseCases(
    val getTasks: GetTasksUseCase,
    val deleteTask: DeleteTaskUseCase
)