package com.example.testtodo.screens.AddScreen

import TodoViewModel
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testtodo.db.TodoEntity


@Composable
fun AddScreen(
    navigateToAddTaskScreen: () -> Unit,
    itemId: Int?,
) {
    val addViewModel: AddViewScreenModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel()

    val selectedTodo by todoViewModel.getTodoById(itemId ?: 0).observeAsState()

    LaunchedEffect(selectedTodo) {
        selectedTodo?.let {
            Log.d("itemIdTitle", "${it.title} ${it.description}")
            addViewModel.title.value = it.title
            addViewModel.description.value = it.description
        }
    }

    Log.d("itemId", "$itemId")
    Log.d("itemIdValues", "${addViewModel.title.value} ${addViewModel.description.value}")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            TextFields(
                customModifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .weight(1f),
                addViewModel,
            )
            Spacer(modifier = Modifier.height(10.dp))
            ButtonRow(
                navigateToAddTaskScreen,
                todoViewModel,
                addViewModel,
                itemId
            )
        }
    }
}




@Composable
fun TextFields(
    customModifier: Modifier,
    addViewModel: AddViewScreenModel,

){
    TextField(
        value = addViewModel.title.value,
        onValueChange = {addViewModel.title.value=it},
        label = {
            Text(text = "Title")
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    )
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = addViewModel.description.value,
        onValueChange = {addViewModel.description.value = it},
        label = {
            Text(text = "Description")
        },
        modifier = customModifier

    )
}

@Composable
fun ButtonRow(
    navigateToAddTaskScreen: ()-> Unit,
    todoViewModel: TodoViewModel,
    addViewModel: AddViewScreenModel,
    itemId:Int?
    ){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Button(
            onClick = {
                addViewModel.clearFields()
                navigateToAddTaskScreen()
                      },
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = "Cansel")
        }
        Button(
            onClick = {
                todoViewModel.insert(
                    TodoEntity(
                        id = itemId ?: 0,
                        title = addViewModel.title.value,
                        description = addViewModel.description.value
                    )
                )
                navigateToAddTaskScreen()
            },
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = "Add")
        }
    }
}
