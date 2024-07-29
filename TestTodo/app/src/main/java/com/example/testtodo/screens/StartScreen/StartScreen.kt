package com.example.testtodo.screens.StartScreen

import TodoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.screens.AddScreen.AddViewScreenModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun StartScreen(
    navController: NavHostController,

){
    val addViewModel: AddViewScreenModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel()
    val allCategories by todoViewModel.allCategories.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)

    ) {
        TodoBlock(
            navController,
            addViewModel,
            todoViewModel,
            allCategories
        )

        ButtonRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp),
            navController,
        )
//        if (addViewModel.isCategoryListVisible.value) {
//            val textFieldPosition = addViewModel.textFieldPosition.value
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp, horizontal = 10.dp)
//                    .offset(y = textFieldPosition.y.dp + 60.dp) // Position dropdown below the TextField
//                    .align(Alignment.TopStart)
//                    .zIndex(1f) // Ensure this box is on top
//                    .clip(RoundedCornerShape(8.dp))
//            ) {
//                CategoryList(
//                    categories = addViewModel.filteredCategories.value,
//                    onCategorySelected = { category ->
//                        addViewModel.selectCategory(category)
//                    },
//                    onCategoryDeleted = { category ->
//                        addViewModel.deleteCategory(category, coroutineScope, todoViewModel)
//                        addViewModel.filterCategories(addViewModel.title.value, allCategories)
//                    }
//                )
//            }
//        }
    }
}

@Composable
fun TodoBlock(
    navController: NavHostController,
    addViewModel: AddViewScreenModel,
    todoViewModel: TodoViewModel,
    allCategories: List<Category>
) {
    val todo: TodoViewModel = viewModel()
    val viewModel: StartScreenViewModel = viewModel()
    val todos by todo.allTodos.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)

    ) {
        SearchBar(
            viewModel = viewModel,
            addViewModel,
            todoViewModel,
            allCategories,
            navController
            )

        TodoColumn(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
            ,
            todos = todos,
            viewModel = viewModel,
            navController
        )
    }
}

@Composable
fun TodoColumn(
    modifier: Modifier,
    todos: List<TodoEntity>,
    viewModel: StartScreenViewModel,
    navController: NavHostController,

) {
    val todoViewModel: TodoViewModel = viewModel()
    val searchText = viewModel.searchTodo.value.trim().lowercase()

    LazyColumn(
        modifier = modifier
    ) {
        items(todos.filter { todo ->
            todo.title.contains(searchText, ignoreCase = true) ||
                    todo.description.contains(searchText, ignoreCase = true)
        }, key = { it.todoId }) { item ->
            TodoItem(
                item = item,
                todoViewModel = todoViewModel,
                navController,
                viewModel
            )
        }
    }

}

@Composable
fun TodoItem(item: TodoEntity,
             todoViewModel: TodoViewModel,
             navController: NavHostController,
             viewModel: StartScreenViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val setOffsetX = remember { mutableStateOf(0f) }

    if(viewModel.showDialog.value){
        ShowAlterDialog(
            onDismiss = {viewModel.hideDialogValue()},
            onConfirm = {
                viewModel.itemToDelete.value?.let {
                    todoViewModel.deleteTodo(it)
                }
                viewModel.hideDialogValue()
            }
        )
    }

    LaunchedEffect(key1 = item.todoId) {
        setOffsetX.value = 0f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp)
            .offset { IntOffset(setOffsetX.value.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (setOffsetX.value > 300f) {
                                viewModel.showDialogValue(item)
                                setOffsetX.value = 0f
                            } else {
                                setOffsetX.value = 0f
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newValue = setOffsetX.value + dragAmount
                            setOffsetX.value = newValue.coerceIn(0f, 1000f)
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        navController.navigate("addScreen/${item.todoId}")
                    }
                )
            }
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
    ) {
        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = item.description,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ShowAlterDialog(
    onDismiss:()-> Unit,
    onConfirm: ()-> Unit,
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Deletion")
        },
        text = {
            Text("Are you sure you want to delete this todo?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
        )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: StartScreenViewModel,
    addViewModel: AddViewScreenModel,
    todoViewModel: TodoViewModel,
    allCategories: List<Category>,
    navController: NavHostController) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            addViewModel = addViewModel,
            allCategories = allCategories,
            viewModel
        )
        IconButton(
            onClick = {navController.navigate("settings") }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}


@Composable
fun TextField(
    addViewModel: AddViewScreenModel,
    allCategories: List<Category>,
    viewModel: StartScreenViewModel,
) {
    Column{
        TextField(
            value = addViewModel.title.value,
            onValueChange = { newValue ->
                addViewModel.title.value = newValue
                addViewModel.filterCategories(newValue, allCategories)
                viewModel.searchTodo.value = newValue
            },
            label = { Text(text = "Search") },
            modifier = Modifier
        )
    }
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
        ,
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ){
        Button(
            onClick = {
                navController.navigate("addScreen/0")
            },
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA33FF),
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(text = "Add Todo")
        }

    }
}
