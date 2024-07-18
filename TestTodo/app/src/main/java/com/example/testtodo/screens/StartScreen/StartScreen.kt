package com.example.testtodo.screens.StartScreen

import TodoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testtodo.db.TodoEntity

@Composable
fun StartScreen(
    navController: NavHostController,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TodoBlock(navController)
        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .size(56.dp),
            navController
        )
    }
}

@Composable
fun TodoBlock(
    navController: NavHostController,
) {
    val todo: TodoViewModel = viewModel()
    val viewModel: StartScreenViewModel = viewModel()
    val todos by todo.allTodos.observeAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(viewModel = viewModel)

        TodoColumn(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
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
        }, key = { it.id }) { item ->
            TodoItem(
                item = item,
                todoViewModel = todoViewModel,
                navController
            )
        }
    }

}

@Composable
fun TodoItem(item: TodoEntity,
             todoViewModel: TodoViewModel,
             navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    val setOffsetX = remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = item.id) {
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
                                todoViewModel.delete(item)
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
                        navController.navigate("addScreen/${item.id}")
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






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: StartScreenViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = viewModel.searchTodo.value,
            onValueChange = { viewModel.searchTodo.value = it },
            label = {
                Text(text = "Search")
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(0.dp)
        )
    }
}


@Composable
fun AddButton(modifier: Modifier = Modifier, navController: NavHostController,){
    Button(
        onClick = {
            navController.navigate("addScreen/0")
                  },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFAA33FF),
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(30.dp)
        )
    }
}
