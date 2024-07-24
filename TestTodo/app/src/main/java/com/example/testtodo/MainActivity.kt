package com.example.testtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.testtodo.screens.NavHost.Host
import com.example.testtodo.ui.theme.TestTODOTheme
import com.example.testtodo.ui.theme.ThemeViewModel


class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTODOTheme(themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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