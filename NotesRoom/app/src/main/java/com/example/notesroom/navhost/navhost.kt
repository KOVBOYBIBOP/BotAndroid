package com.example.notesroom.navhost

import AddTaskScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesroom.StartScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Host(navController: NavHostController) {
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(
                navigateToAddTaskScreen = {
                    navController.navigate("addTask")
                }
            )
        }
        composable("addTask") {
            AddTaskScreen(
                navigateToStartScreen = {
                    navController.navigate("start")
                }
            )
        }
    }
}