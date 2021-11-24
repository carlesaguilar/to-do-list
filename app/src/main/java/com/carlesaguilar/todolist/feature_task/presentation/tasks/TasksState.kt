package com.carlesaguilar.todolist.feature_task.presentation.tasks

import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.util.OrderType
import com.carlesaguilar.todolist.feature_task.domain.util.TaskOrder

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val taskOrder: TaskOrder = TaskOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)