package com.carlesaguilar.todolist.feature_task.domain.use_case

import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.repository.TaskRepository
import com.carlesaguilar.todolist.feature_task.domain.util.OrderType
import com.carlesaguilar.todolist.feature_task.domain.util.TaskOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Date(OrderType.Ascending)
    ): Flow<List<Task>> {
        return repository.getTasks().map { tasks ->
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder) {
                        is TaskOrder.Title -> tasks.sortedBy { it.title.lowercase() }
                        is TaskOrder.Date -> tasks.sortedBy { it.timestamp }
                        is TaskOrder.Color -> tasks.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder) {
                        is TaskOrder.Title -> tasks.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Date -> tasks.sortedByDescending { it.timestamp }
                        is TaskOrder.Color -> tasks.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}