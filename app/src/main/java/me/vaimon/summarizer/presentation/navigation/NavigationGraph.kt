package me.vaimon.summarizer.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.vaimon.summarizer.presentation.screens.home.HomeScreen
import me.vaimon.summarizer.presentation.screens.home.HomeScreenDestination

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route
    ) {
        val springAnimationSpec = spring<IntOffset>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )

        composable(route = HomeScreenDestination.route) {
            HomeScreen(navController)
        }
    }
}