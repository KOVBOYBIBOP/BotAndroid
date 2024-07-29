package com.example.testtodo.screens.NavHost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testtodo.screens.AddScreen.AddScreen
import com.example.testtodo.screens.Categories.CategoriesScreen
import com.example.testtodo.screens.SettingsScreen.SettingsScreen
import com.example.testtodo.screens.StartScreen.StartScreen
import com.example.testtodo.ui.theme.ThemeViewModel

@Composable
fun Host(navController: NavHostController = rememberNavController(), themeViewModel: ThemeViewModel) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("addScreen/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toIntOrNull() ?: 0
            AddScreen(
                { navController.navigate("start") },
                todoId,
            )
        }
        composable("settings") {
            SettingsScreen(
                navController,
                themeViewModel
            )
        }
        composable("categories/{previousScreen}") { backStackEntry ->
            val previousScreen = backStackEntry.arguments?.getString("previousScreen")
            CategoriesScreen(
                navController,
                previousScreen
            )
        }
    }
}


