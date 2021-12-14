package com.carlesaguilar.todolist.feature_task.presentation.add_edit_task

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.carlesaguilar.todolist.core.util.TestTags
import com.carlesaguilar.todolist.feature_task.domain.model.Task
import com.carlesaguilar.todolist.feature_task.presentation.add_edit_task.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditTaskScreen(
    navController: NavController,
    taskColor: Int,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val titleState = viewModel.taskTitle.value
    val scaffoldState = rememberScaffoldState()
    val taskBackgroundAnimatable = remember {
        Animatable(
            Color(if (taskColor != -1) taskColor else viewModel.taskColor.value)
        )
    }
    val scope = rememberCoroutineScope()
    val isTaskCompleted = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditTaskViewModel.UiEvent.SaveTask -> {
                    navController.navigateUp()
                }
                is AddEditTaskViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditTaskEvent.SaveTask)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save"
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(taskBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Task.taskColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.taskColor.value == colorInt) {
                                    Color.Black
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    taskBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditTaskEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Checkbox(
                    checked = viewModel.taskIsCompleted.value,
                    onCheckedChange = {
                        isTaskCompleted.value = it
                        viewModel.onEvent(AddEditTaskEvent.IsCompleted(isTaskCompleted.value))
                    },
                    colors = CheckboxDefaults.colors(Color.Black)
                )
                Spacer(modifier = Modifier.width(10.dp))
                ClickableText(
                    text = AnnotatedString("Mark as completed"),
                    onClick = {
                        isTaskCompleted.value = !isTaskCompleted.value
                        viewModel.onEvent(AddEditTaskEvent.IsCompleted(isTaskCompleted.value))
                    },
                    style = MaterialTheme.typography.h6
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTaskEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTaskEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.h5,
                testTag = TestTags.TITLE_TEXT_FIELD
            )
        }
    }
}