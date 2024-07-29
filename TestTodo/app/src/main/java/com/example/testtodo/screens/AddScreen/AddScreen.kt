package com.example.testtodo.screens.AddScreen

import TodoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.screens.Categories.CategoriesRow
import com.example.testtodo.screens.Categories.CategoriesViewModel
import com.example.testtodo.ui.theme.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun AddScreen(
    navigateToAddTaskScreen: () -> Unit,
    itemId: Int?,
) {
    val addViewModel: AddViewScreenModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val id = itemId ?: 0

    val todoWithCategories by todoViewModel.getTodoWithCategories(id).observeAsState()
    val selectedTodo by todoViewModel.getTodoById(id).observeAsState()
    val allCategories by todoViewModel.allCategories.observeAsState(emptyList())

    LaunchedEffect(selectedTodo) {
        selectedTodo?.let {
            addViewModel.title.value = it.title
            addViewModel.description.value = it.description
            addViewModel.selectedCategories.value = todoWithCategories?.categories ?: emptyList()
        }
    }


    val categoryMenuPosition = remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextFields(
                addViewModel = addViewModel,
                onDropdownMenuPositionUpdated = { coordinates ->
                    categoryMenuPosition.value = coordinates.positionInWindow()
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            ButtonRow(
                navigateToAddTaskScreen = navigateToAddTaskScreen,
                todoViewModel = todoViewModel,
                addViewModel = addViewModel,
                itemId = id,
                coroutineScope = coroutineScope,
            )
        }

        if (addViewModel.isCategoryListVisible.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = categoryMenuPosition.value.y.dp, start = categoryMenuPosition.value.x.dp)
                    .background(Color.Transparent)
            ) {
                DropdownMenu(
                    expanded = addViewModel.isCategoryListVisible.value,
                    onDismissRequest = { addViewModel.hideCategoryList() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                ) {
                    allCategories.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                val currentCategories = addViewModel.selectedCategories.value.toMutableList()
                                if (category !in currentCategories) {
                                    currentCategories.add(category)
                                    addViewModel.selectedCategories.value = currentCategories
                                }
                                addViewModel.hideCategoryList()
                            },
                            text = { Text(text = category.name, color = Color(category.color)) }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun TextFields(
    addViewModel: AddViewScreenModel,
    onDropdownMenuPositionUpdated: (LayoutCoordinates) -> Unit
) {
    Column {
        TextField(
            value = addViewModel.title.value,
            onValueChange = { addViewModel.title.value = it },
            label = { Text(text = "Title") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = addViewModel.description.value,
            onValueChange = { addViewModel.description.value = it },
            label = { Text(text = "Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(addViewModel.selectedCategories.value) { item ->
                CategoriesTodoRow(item)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                addViewModel.showCategoryList()
            },
            modifier = Modifier.onGloballyPositioned { coordinates ->
                onDropdownMenuPositionUpdated(coordinates)
            }
        ) {
            Text(text = "Add Category")
        }
    }
}





@Composable
fun CategoriesTodoRow(
    item: Category,
) {
    val selectedColor = Color(item.color)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Text(
            item.name,
            color = selectedColor,
        )

        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(selectedColor, shape = CircleShape)
        )
    }
}

//@Composable
//fun CategoryList(
//    categories: List<Category>,
//    addViewModel: AddViewScreenModel
//) {
//    LazyColumn(
//        modifier = Modifier
//            .clip(RoundedCornerShape(8.dp))
//            .background(Color.White)
//    ) {
//        items(categories) { category ->
//            Row(
//                modifier = Modifier
//                    .padding(10.dp)
//                    .clickable {
//                        val currentCategories = addViewModel.selectedCategories.value.toMutableList()
//                        if (category !in currentCategories) {
//                            currentCategories.add(category)
//                            addViewModel.selectedCategories.value = currentCategories
//                        }
//                    },
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    category.name,
//                    color = Color( category.color),
//                )
//                Spacer(modifier = Modifier.width(20.dp))
//                Box(
//                    modifier = Modifier
//                        .size(24.dp)
//                        .background(Color(category.color), shape = CircleShape)
//                )
//            }
//        }
//    }
//}


@Composable
fun ButtonRow(
    navigateToAddTaskScreen: () -> Unit,
    todoViewModel: TodoViewModel,
    addViewModel: AddViewScreenModel,
    itemId: Int?,
    coroutineScope: CoroutineScope,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = {
                addViewModel.clearFields()
                navigateToAddTaskScreen()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Cancel")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    val todo = TodoEntity(
                        todoId = itemId ?: 0,
                        title = addViewModel.title.value,
                        description = addViewModel.description.value,
                    )
                    todoViewModel.insertTodo(todo)
                    addViewModel.selectedCategories.value.forEach { category ->
                        todoViewModel.insertTodoCategoryCrossRef(todo.todoId, category.categoryId)
                    }
                }

                navigateToAddTaskScreen()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Add")
        }
    }
}

