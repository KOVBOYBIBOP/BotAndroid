package com.example.testtodo.screens.AddScreen

import TodoViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.lifecycle.ViewModel
import com.example.testtodo.db.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AddViewScreenModel : ViewModel() {
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val isCategoryListVisible: MutableState<Boolean> = mutableStateOf(false)
    val filteredCategories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val textFieldPosition = mutableStateOf(Offset.Zero)

    fun clearFields() {
        title.value = ""
        description.value = ""
    }

    fun filterCategories(query: String, categories: List<Category>) {
        filteredCategories.value = if (query.isEmpty()) {
            categories
        } else {
            categories.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    fun showCategoryList() {
        isCategoryListVisible.value = true
    }

    fun hideCategoryList() {
        isCategoryListVisible.value = false
    }

    fun selectCategory(category: Category) {
        title.value = category.name
        hideCategoryList()
    }

    fun updateTextFieldPosition(coordinates: LayoutCoordinates) {
        val positionInWindow = coordinates.positionInWindow()
        textFieldPosition.value = Offset(positionInWindow.x, positionInWindow.y)
    }

    fun deleteCategory(
        category: Category,
        scope: CoroutineScope,
        todoViewModel: TodoViewModel
    ) {
        scope.launch {
            todoViewModel.deleteCategory(category)
        }
    }
}

