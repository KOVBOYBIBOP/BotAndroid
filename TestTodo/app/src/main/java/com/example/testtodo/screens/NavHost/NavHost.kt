package com.example.testtodo.screens.NavHost

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testtodo.screens.AddScreen.AddScreen
import com.example.testtodo.screens.StartScreen.StartScreen

@Composable
fun Host(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("addScreen/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toIntOrNull() ?: 0
            Log.d("Host_check", "$todoId")
            AddScreen(
                { navController.navigate("start") },
                todoId
                )
        }
    }
}
