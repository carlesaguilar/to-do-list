package com.carlesaguilar.todolist.feature_task.domain.use_case

import androidx.compose.ui.graphics.toArgb
import com.carlesaguilar.todolist.feature_task.data.repository.FakeTaskRepository
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.ui.theme.RedOrange
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteTaskUseCaseTest {
    private lateinit var deleteTask: DeleteTaskUseCase
    private lateinit var fakeRepository: FakeTaskRepository
    private val taskToDelete = Task(
        title = "task to delete",
        timestamp = System.currentTimeMillis(),
        color = RedOrange.toArgb(),
        isCompleted = false,
        id = 1
    )

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        deleteTask = DeleteTaskUseCase(fakeRepository)

        runBlocking {
            fakeRepository.insertTask(taskToDelete)
        }
    }

    @Test
    fun `check task is correctly deleted`() = runBlocking {
        deleteTask(taskToDelete)

        val deletedTask = fakeRepository.getTaskById(1)
        assertThat(deletedTask).isEqualTo(null)
    }
}