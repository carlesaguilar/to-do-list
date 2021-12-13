package com.carlesaguilar.todolist.feature_task.domain.use_case

import com.carlesaguilar.todolist.feature_task.data.repository.FakeTaskRepository
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.domain.util.OrderType
import com.carlesaguilar.todolist.feature_task.domain.util.TaskOrder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTasksUseCaseTest {
    private lateinit var getTasks: GetTasksUseCase
    private lateinit var fakeRepository: FakeTaskRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        getTasks = GetTasksUseCase(fakeRepository)

        val tasksToInsert = mutableListOf<Task>()
        ('a'..'z').forEachIndexed { index, c ->
            tasksToInsert.add(
                Task(
                    title = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        tasksToInsert.shuffle()
        runBlocking {
            tasksToInsert.forEach { fakeRepository.insertTask(it) }
        }
    }

    @Test
    fun `Order notes by title ascending`() {
        runBlocking {
            val tasks = getTasks(taskOrder = TaskOrder.Title(OrderType.Ascending)).first()
            for (i in 0..tasks.size - 2) {
                assertThat(tasks[i].title).isLessThan(tasks[i + 1].title)
            }
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getTasks(TaskOrder.Title(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getTasks(TaskOrder.Date(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getTasks(TaskOrder.Date(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getTasks(TaskOrder.Color(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i + 1].color)
        }
    }

    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getTasks(TaskOrder.Color(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i + 1].color)
        }
    }
}