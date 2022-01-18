package com.carlesaguilar.todolist.feature_task.presentation.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlesaguilar.todolist.core.util.TestTags
import com.carlesaguilar.todolist.di.AppModule
import com.carlesaguilar.todolist.feature_task.presentation.MainActivity
import com.carlesaguilar.todolist.feature_task.presentation.add_edit_task.AddEditTaskScreen
import com.carlesaguilar.todolist.feature_task.presentation.util.Screen
import com.carlesaguilar.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AddEditTaskScreenTest {

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
            ToDoListTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.AddEditTaskScreen.route
                ) {
                    composable(route = Screen.AddEditTaskScreen.route) {
                        val color = it.arguments?.getInt("taskColor") ?: -1
                        AddEditTaskScreen(
                            navController = navController,
                            taskColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun clickMarkAsCompleted() {
        composeRule.onNodeWithTag(TestTags.LABEL_MARK_AS_COMPLETED).performClick()
        composeRule.onNodeWithTag(TestTags.CHECKBOX_COMPLETED).assertIsOn()
    }

    @Test
    fun checkHintVisible() {
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD + "_HINT").assertIsDisplayed()
    }

    @Test
    fun checkTaskTextIsEmpty() {
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("")
    }
}