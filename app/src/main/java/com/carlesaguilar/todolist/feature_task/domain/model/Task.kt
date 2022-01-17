package com.carlesaguilar.todolist.feature_task.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlesaguilar.todolist.ui.theme.*

@Entity
data class Task(
    val title: String,
    val timestamp: Long,
    val color: Int,
    var isCompleted: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val taskColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidTaskException(message: String) : Exception(message)