package com.carlesaguilar.todolist.feature_task.presentation

data class TaskTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)