package com.carlesaguilar.todolist.feature_task.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlesaguilar.todolist.feature_task.presentation.add_edit_task.AddEditTaskScreen
import com.carlesaguilar.todolist.feature_task.presentation.tasks.TasksScreen
import com.carlesaguilar.todolist.feature_task.presentation.util.Screen
import com.carlesaguilar.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
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
        }
    }
}