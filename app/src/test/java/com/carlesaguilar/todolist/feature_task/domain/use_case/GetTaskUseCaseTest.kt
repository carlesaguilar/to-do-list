package com.carlesaguilar.todolist.feature_task.domain.use_case

import com.carlesaguilar.todolist.feature_task.data.repository.FakeTaskRepository
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTaskUseCaseTest {
    private lateinit var getTask: GetTaskUseCase
    private lateinit var fakeRepository: FakeTaskRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        getTask = GetTaskUseCase(fakeRepository)

        val tasksToInsert = mutableListOf<Task>()
        ('a'..'z').forEachIndexed { index, c ->
            tasksToInsert.add(
                Task(
                    title = c.toString(),
                    timestamp = index.toLong(),
                    color = index,
                    id = index + 1
                )
            )
        }
        tasksToInsert.shuffle()
        runBlocking {
            tasksToInsert.forEach { fakeRepository.insertTask(it) }
        }
    }

    @Test
    fun `Get task with id 1`() = runBlocking {
        val task = getTask(1)
        assertThat(task?.title).isEqualTo("a")
    }

    @Test
    fun `Get task with id 14`() = runBlocking {
        val task = getTask(14)
        assertThat(task?.title).isEqualTo("n")
    }

    @Test
    fun `Get task with id 26`() = runBlocking {
        val task = getTask(26)
        assertThat(task?.title).isEqualTo("z")
    }
}