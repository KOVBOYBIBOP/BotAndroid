package com.example.testtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import com.example.testtodo.screens.AddScreen.AddScreen
import com.example.testtodo.screens.Categories.CategoriesScreen
import com.example.testtodo.screens.NavHost.Host
import com.example.testtodo.screens.SettingsScreen.SettingsScreen
import com.example.testtodo.ui.theme.TestTODOTheme
import com.example.testtodo.ui.theme.ThemeViewModel
import java.lang.reflect.Modifier


class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTODOTheme(themeViewModel) {
//                AddScreen(
//                    navigateToAddTaskScreen = { /*TODO*/ },
//                    itemId = null,
//                    themeViewModel = themeViewModel
//                )
//                CategoriesScreen()
//                SettingsScreen()
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                ) {
                    Host(themeViewModel = themeViewModel)
                }
            }
        }
    }
}

//class MainActivity :ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Host()
//        }
//    }
//}