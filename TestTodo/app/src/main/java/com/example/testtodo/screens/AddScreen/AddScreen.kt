package com.example.testtodo.screens.AddScreen

import TodoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testtodo.R
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.ui.theme.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AddScreen(
    navigateToAddTaskScreen: () -> Unit,
    itemId: Int?,
    themeViewModel: ThemeViewModel
) {
    val addViewModel: AddViewScreenModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel()
    val allCategories by todoViewModel.allCategories.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    val selectedTodo by todoViewModel.getTodoById(itemId ?: 0).observeAsState()

    LaunchedEffect(selectedTodo) {
        selectedTodo?.let {
            addViewModel.title.value = it.title
            addViewModel.description.value = it.description
        }
    }

    Box(modifier = Modifier
        .fillMaxSize().background(MaterialTheme.colorScheme.surface)
        ,
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
            ,

            ) {
            TextFields(
                addViewModel = addViewModel,
                allCategories = allCategories
            )
            Spacer(modifier = Modifier.height(10.dp).background(MaterialTheme.colorScheme.surface)
            )
            ButtonRow(
                navigateToAddTaskScreen = navigateToAddTaskScreen,
                todoViewModel = todoViewModel,
                addViewModel = addViewModel,
                itemId = itemId,
                allCategories = allCategories,
                coroutineScope = coroutineScope,
                themeViewModel = themeViewModel
            )
        }

        // Display category list on top
        if (addViewModel.isCategoryListVisible.value) {
            val textFieldPosition = addViewModel.textFieldPosition.value
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp)
                    .offset(y = textFieldPosition.y.dp + 60.dp)
                    .align(Alignment.TopStart)
                    .zIndex(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                CategoryList(
                    categories = addViewModel.filteredCategories.value,
                    onCategorySelected = { category ->
                        addViewModel.selectCategory(category)
                    },
                    onCategoryDeleted = { category ->
                        addViewModel.deleteCategory(category, coroutineScope, todoViewModel)
                        addViewModel.filterCategories(addViewModel.title.value, allCategories)
                    }
                )
            }
        }
    }
}

@Composable
fun TextFields(
    addViewModel: AddViewScreenModel,
    allCategories: List<Category>
) {
    Column{
        TextField(
            value = addViewModel.title.value,
            onValueChange = { newValue ->
                addViewModel.title.value = newValue
                addViewModel.showCategoryList()
                addViewModel.filterCategories(newValue, allCategories)
            },
            label = { Text(text = "Title") },
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)

                .onGloballyPositioned { coordinates ->
                    addViewModel.updateTextFieldPosition(coordinates)
                }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = addViewModel.description.value,
            onValueChange = { addViewModel.description.value = it },
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    onCategoryDeleted: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        items(categories) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable { onCategorySelected(category) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = category.name)
                Button(onClick = { onCategoryDeleted(category) }) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun ButtonRow(
    navigateToAddTaskScreen: () -> Unit,
    todoViewModel: TodoViewModel,
    addViewModel: AddViewScreenModel,
    itemId: Int?,
    allCategories: List<Category>,
    coroutineScope: CoroutineScope,
    themeViewModel: ThemeViewModel
) {
    val categoryName = addViewModel.title.value
    var categoryId by remember(categoryName) {
        mutableStateOf<Int?>(null)
    }

    // Найдите существующую категорию, если она есть
    LaunchedEffect(allCategories) {
        val existingCategory = allCategories.find { it.name == categoryName }
        categoryId = existingCategory?.id
    }

    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
        ,
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
                    // Проверьте, есть ли уже категория, иначе добавьте её
                    val category = allCategories.find { it.name == categoryName }
                    val catId = category?.id ?: run {
                        if (categoryName.isNotBlank()) {
                            val newCategory = Category(name = categoryName)
                            todoViewModel.insertTCategory(newCategory)
                            // Обновите категорию после вставки
                            todoViewModel.allCategories.value?.find { it.name == categoryName }?.id
                        } else null
                    }

                    // Добавьте задачу с правильным идентификатором категории
                    todoViewModel.insertTodo(
                        TodoEntity(
                            id = itemId ?: 0,
                            title = categoryName,
                            description = addViewModel.description.value,
                            categoryId = catId
                        )
                    )
                }
                navigateToAddTaskScreen()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Add")
        }

        IconButton(
            onClick = {
                themeViewModel.toggleTheme()
            }
        ) {
            Icon(
                painter = painterResource(
                    id = if (themeViewModel.isDarkTheme.value) R.drawable.ic_sun else R.drawable.ic_moon
                ),
                contentDescription = "Toggle Theme",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


