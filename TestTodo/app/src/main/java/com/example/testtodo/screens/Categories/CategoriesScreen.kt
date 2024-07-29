package com.example.testtodo.screens.Categories

import TodoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testtodo.db.Category
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CategoriesScreen(
    navController: NavHostController,
    previousScreen: String?
) {

    var showAddDialog by remember { mutableStateOf(false) }
    var categoryIdToEdit by remember { mutableStateOf<Int?>(null) }
    val CategoriesviewModel: CategoriesViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBarCategories(
            onAddClicked = {
                showAddDialog = true
                categoryIdToEdit = 0
                           },
            navController,
            previousScreen
        ) },
        content = { innerPaddingValues ->
            ContentCategorise(
                innerPaddingValues,
                showAddDialog,
                onCategoryLongPress = { id ->
                    categoryIdToEdit = id
                    showAddDialog = true
                }
            )
        }
    )

    if (showAddDialog) {
        AddNewCategories(
            onDismiss = { showAddDialog = false },
            CategoriesviewModel = CategoriesviewModel,
            CategoryId = categoryIdToEdit ?: 0
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCategories(
    onAddClicked: () -> Unit,
    navController: NavHostController,
    previousScreen: String?
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.displayLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                previousScreen?.let {
                    navController.navigate(it) {
                        popUpTo("categories") { inclusive = true }
                    }
                } ?: navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onAddClicked) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}


@Composable
fun ContentCategorise(
    innerPaddingValues: PaddingValues,
    showAddDialog: Boolean,
    onCategoryLongPress: (Int) -> Unit
) {
    val todo: TodoViewModel = viewModel()
    val categories by todo.allCategories.observeAsState(emptyList())
    val viewModel: CategoriesViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
    ) {
        CategoriesColumn(
            categories,
            viewModel,
            showAddDialog,
            onCategoryLongPress
        )
    }
}





@Composable
fun AddNewCategories(
    viewModel: TodoViewModel = viewModel(),
    onDismiss: () -> Unit,
    CategoriesviewModel: CategoriesViewModel,
    CategoryId: Int = 0
) {

    val selectedCategory by viewModel.getCategoryById(CategoryId).observeAsState()

//    var categoryName = CategoriesviewModel.name.value
//    var colorPickerExpanded by remember { mutableStateOf(false) }
//    var selectedColor = CategoriesviewModel.color.value

    var categoryName by remember { mutableStateOf(CategoriesviewModel.name.value) }
    var colorPickerExpanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(CategoriesviewModel.color.value) }


    LaunchedEffect(selectedCategory) {
        selectedCategory?.let {
            categoryName = it.name // Sync local state with ViewModel
            selectedColor = Color(it.color) // Sync local state with ViewModel
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add New Category") },
        text = {
            Column {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { colorPickerExpanded = !colorPickerExpanded }
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(selectedColor, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Color")
                }

                DropdownMenu(
                    expanded = colorPickerExpanded,
                    onDismissRequest = { colorPickerExpanded = false }
                ) {
                    CategoriesviewModel.colors.forEach { colorCategory ->
                        DropdownMenuItem(
                            onClick = {
                                selectedColor = colorCategory
                                colorPickerExpanded = false
                            },
                            text = {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(colorCategory, shape = CircleShape)
                                )
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        val categoryColor = selectedColor.toArgb() // Convert Color to Int
                        val newCategory = Category(categoryId = CategoryId, name = categoryName, color = categoryColor)
                        viewModel.insertCategory(newCategory)
                        onDismiss() // Close the dialog
                    }
                }
            ) {
                Text("Add Category")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun CategoriesColumn(
    categories: List<Category>,
    viewModel: CategoriesViewModel,
    showAddDialog: Boolean,
    onCategoryLongPress: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(categories) { item ->
            CategoriesRow(item, viewModel, showAddDialog, onCategoryLongPress)
        }
    }
}



@Composable
fun CategoriesRow(
    item: Category,
    viewModel: CategoriesViewModel,
    showAddDialog: Boolean,
    onCategoryLongPress: (Int) -> Unit,
    TodoviewModel: TodoViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val setOffsetX = remember { mutableStateOf(0f) }
    var expanded by remember { mutableStateOf(false) }
    var selectedColor = Color(item.color)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .offset { IntOffset(setOffsetX.value.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (setOffsetX.value > 300f) {
                                TodoviewModel.deleteCategory(item)
                            }
                            setOffsetX.value = 0f
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            setOffsetX.value = (setOffsetX.value + dragAmount).coerceIn(0f, 1000f)
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onCategoryLongPress(item.categoryId) }
                )
            }
    ) {
        Text(
            item.name,
            color = selectedColor,
        )

        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { expanded = !expanded }
                .background(selectedColor, shape = CircleShape)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }
        ) {
            viewModel.colors.forEach { color ->
                DropdownMenuItem(
                    onClick = {
                        val updatedCategory = item.copy(color = color.toArgb())
                        TodoviewModel.updateCategory(updatedCategory)
                        selectedColor = color
                        expanded = !expanded
                    },
                    text = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(color, shape = CircleShape)
                        )
                    }
                )
            }
        }
    }
}


