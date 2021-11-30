package com.carlesaguilar.todolist.feature_task.domain.util

sealed class TaskOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : TaskOrder(orderType)
    class Date(orderType: OrderType) : TaskOrder(orderType)
    class Color(orderType: OrderType) : TaskOrder(orderType)

    fun copy(orderType: OrderType): TaskOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}