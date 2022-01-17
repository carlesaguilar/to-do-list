package com.carlesaguilar.todolist.feature_task.domain.use_case

import androidx.compose.ui.graphics.toArgb
import com.carlesaguilar.todolist.feature_task.data.repository.FakeTaskRepository
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.ui.theme.LightBlue
import com.carlesaguilar.todolist.ui.theme.RedOrange
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddTaskUseCaseTest {
    private lateinit var addTask: AddTaskUseCase
    private lateinit var fakeRepository: FakeTaskRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        addTask = AddTaskUseCase(fakeRepository)

        val taskToInsert1 = Task(
            title = "my task title",
            timestamp = System.currentTimeMillis(),
            color = RedOrange.toArgb(),
            isCompleted = true
        )

        val taskToInsert2 = Task(
            title = "my task title 2",
            timestamp = System.currentTimeMillis(),
            color = LightBlue.toArgb(),
            isCompleted = false
        )

        runBlocking {
            fakeRepository.insertTask(taskToInsert1)
            fakeRepository.insertTask(taskToInsert2)
        }
    }

    @Test
    fun `check inserted task is correct`() = runBlocking {
        fakeRepository.getTasks().collect { tasksList ->
            val task1 = tasksList[0]
            assertThat(task1.title).isEqualTo("my task title")
            assertThat(task1.timestamp).isNotEqualTo(0)
            assertThat(task1.color).isEqualTo(RedOrange.toArgb())
            assertThat(task1.isCompleted).isEqualTo(true)

            val task2 = tasksList[1]
            assertThat(task2.title).isEqualTo("my task title 2")
            assertThat(task2.timestamp).isNotEqualTo(0)
            assertThat(task2.color).isEqualTo(LightBlue.toArgb())
            assertThat(task2.isCompleted).isEqualTo(false)
        }
    }
}