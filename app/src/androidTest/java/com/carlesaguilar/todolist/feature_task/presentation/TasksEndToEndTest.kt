package com.carlesaguilar.todolist.feature_task.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlesaguilar.todolist.core.util.TestTags
import com.carlesaguilar.todolist.di.AppModule
import com.carlesaguilar.todolist.feature_task.presentation.add_edit_task.AddEditTaskScreen
import com.carlesaguilar.todolist.feature_task.presentation.tasks.TasksScreen
import com.carlesaguilar.todolist.feature_task.presentation.util.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TasksEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.TasksScreen.route
            ) {
                composable(route = Screen.TasksScreen.route) {
                    TasksScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditTaskScreen.route + "?taskId={taskId}&taskColor={taskColor}",
                    arguments = listOf(
                        navArgument(name = "taskId") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument(name = "taskColor") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    val color = it.arguments?.getInt("taskColor") ?: -1
                    AddEditTaskScreen(
                        navController = navController,
                        taskColor = color
                    )
                }
            }
        }
    }

    @Test
    fun saveNewTask_editAfterwarks() {
        val textToSave = "test-task"
        val textToAppend = "2"

        // click on FAB to get add task screen
        composeRule
            .onNodeWithContentDescription("Add task")
            .performClick()

        // Create new task with text
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput(textToSave)
        composeRule
            .onNodeWithContentDescription("Save")
            .performClick()

        // Check the task has been created
        composeRule
            .onNodeWithText(textToSave)
            .assertIsDisplayed()

        // Click on task to edit it
        composeRule
            .onNodeWithText(textToSave)
            .performClick()

        // Check if the task has the correct title
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals(textToSave)

        // Add the text "2" to the task title
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput(textToAppend)

        // Update the task
        composeRule
            .onNodeWithContentDescription("Save")
            .performClick()

        // Check the update is working
        composeRule
            .onNodeWithText(textToSave + textToAppend)
            .assertIsDisplayed()
    }

    @Test
    fun saveNewTasks_orderByTitleDescending() {
        for (i in 1..3) {
            composeRule
                .onNodeWithContentDescription("Add task")
                .performClick()

            composeRule
                .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput(i.toString())

            composeRule
                .onNodeWithContentDescription("Save")
                .performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("1")
    }
}