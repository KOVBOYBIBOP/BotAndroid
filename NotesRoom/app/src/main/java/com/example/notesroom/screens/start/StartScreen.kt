// StartScreen.kt
package com.example.notesroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesroom.model.Todo
import com.example.notesroom.model.StartViewModel
import com.example.notesroom.model.TodoViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StartScreen(navigateToAddTaskScreen: () -> Unit) {
    val viewModel = viewModel<StartViewModel>()
    val TodoViewModel = viewModel<TodoViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.taskTitle,
                onValueChange = { viewModel.taskTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text("Search") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text
                ),
                maxLines = 1
            )

            LazyColumn {
                itemsIndexed(TodoViewModel.getAllTodo()) { index, item ->
                    TodoItem(
                        item = item,
                        onDelete = { viewModel.deleteItem(it) },
                        onEdit = { /* Handle edit logic here */ },
                        viewModel = viewModel
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { navigateToAddTaskScreen() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
                tint = Color.DarkGray
            )
        }
    }
}



// TodoItem.kt
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoItem(item: Todo, onDelete: (Todo) -> Unit, onEdit: (Todo) -> Unit, viewModel: StartViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onEdit(item) }
                )
            }
            .offset { IntOffset(viewModel.offsetX.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (viewModel.offsetX > 300f) {
                            coroutineScope.launch {
                                viewModel.setOffsetX(1000f)
                                onDelete(item)
                            }
                        } else {
                            coroutineScope.launch {
                                viewModel.setOffsetX(0f)
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newValue = viewModel.offsetX + dragAmount
                            viewModel.setOffsetX(newValue.coerceIn(0f, 1000f))
                        }
                    }
                )
            }
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(item.body, style = MaterialTheme.typography.bodyLarge)
    }
}
