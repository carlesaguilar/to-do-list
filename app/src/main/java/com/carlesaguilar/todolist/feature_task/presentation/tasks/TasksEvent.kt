package com.carlesaguilar.todolist.feature_task.presentation.tasks

import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.util.TaskOrder

sealed class TasksEvent {
    data class Order(val taskOrder: TaskOrder) : TasksEvent()
    data class OnCompleteTask(val task: Task) : TasksEvent()
    data class DeleteTask(val task: Task) : TasksEvent()
    object RestoreTask : TasksEvent()
    object ToggleOrderSection : TasksEvent()
}