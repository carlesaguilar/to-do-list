package com.carlesaguilar.todolist.feature_task.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}