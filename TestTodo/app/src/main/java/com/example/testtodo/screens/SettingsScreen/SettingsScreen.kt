package com.example.testtodo.screens.SettingsScreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testtodo.ui.theme.ThemeViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderSettings {
                navController.navigate("start")
            }
        }
    ) { innerPadding ->
        ContentSettings(
            innerPadding,
            themeViewModel
        ) { navController.navigate("categories/settings") }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSettings(
    navigateToCategoriesScreen: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayLarge
            )
                },
        navigationIcon = {
            IconButton(onClick = navigateToCategoriesScreen) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "BackButton")
            }
        }
    )
}

@Composable
fun ContentSettings(
    innerPaddingValues: PaddingValues,
    themeViewModel: ThemeViewModel,
    navigateToCategoriesScreen: () -> Unit
){
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .padding(16.dp)
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SelectTheme(themeViewModel)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier =  Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = navigateToCategoriesScreen,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Manage categories")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTheme(themeViewModel: ThemeViewModel) {
    ExposedDropdownMenuBox(
        expanded = themeViewModel.expanded.value,
        onExpandedChange = { themeViewModel.expanded.value = !themeViewModel.expanded.value }
    ) {
        TextField(
            value = themeViewModel.selectedOption.value,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Select Theme") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = themeViewModel.expanded.value
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            interactionSource = remember { MutableInteractionSource() }
        )
        ExposedDropdownMenu(
            expanded = themeViewModel.expanded.value,
            onDismissRequest = { themeViewModel.expanded.value = false }
        ) {
            themeViewModel.themeOptions.forEach { item ->
                androidx.compose.material.DropdownMenuItem(
                    onClick = {
                        themeViewModel.updateThemeSelection(item)
                        themeViewModel.expanded.value = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}
