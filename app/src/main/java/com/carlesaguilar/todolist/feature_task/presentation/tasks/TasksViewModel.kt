package com.carlesaguilar.todolist.feature_task.presentation.tasks

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.use_case.TaskUseCases
import com.carlesaguilar.todolist.feature_task.domain.util.OrderType
import com.carlesaguilar.todolist.feature_task.domain.util.TaskOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _state = mutableStateOf(TasksState())
    val state: State<TasksState> = _state

    private var deletedTask: Task? = null
    private var getTasksJob: Job? = null

    init {
        getTasks(TaskOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.OnCompleteTask -> {
                viewModelScope.launch {
                    taskUseCases.addTask(event.task)
                }
            }
            is TasksEvent.DeleteTask -> {
                viewModelScope.launch {
                    taskUseCases.deleteTask(event.task)
                    deletedTask = event.task
                }
            }
            is TasksEvent.RestoreTask -> {
                viewModelScope.launch {
                    taskUseCases.addTask(deletedTask ?: return@launch)
                    deletedTask = null
                }
            }
            is TasksEvent.Order -> {
                if (state.value.taskOrder::class == event.taskOrder::class &&
                    state.value.taskOrder.orderType == event.taskOrder.orderType
                ) {
                    return
                }
                getTasks(event.taskOrder)
            }
            is TasksEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun getTasks(taskOrder: TaskOrder) {
        getTasksJob?.cancel()
        getTasksJob = taskUseCases.getTasks(taskOrder).onEach { tasks ->
            _state.value = state.value.copy(
                tasks = tasks,
                taskOrder = taskOrder
            )
        }.launchIn(viewModelScope)
    }
}