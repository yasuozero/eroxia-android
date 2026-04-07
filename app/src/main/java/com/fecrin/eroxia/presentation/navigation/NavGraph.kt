package com.fecrin.eroxia.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.fecrin.eroxia.presentation.screens.control.ControlScreen
import com.fecrin.eroxia.presentation.screens.home.HomeScreen
import com.fecrin.eroxia.presentation.screens.viewer.ViewerScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(navController = navController)
        }
        composable<Control> {
            ControlScreen()
        }
        composable<Viewer> {
            ViewerScreen()
        }

    }
}