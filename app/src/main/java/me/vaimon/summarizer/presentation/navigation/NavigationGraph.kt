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
import me.vaimon.summarizer.presentation.screens.scanner.ScannerDestination
import me.vaimon.summarizer.presentation.screens.scanner.ScannerScreen
import me.vaimon.summarizer.presentation.screens.summarization.SummarizationDestination
import me.vaimon.summarizer.presentation.screens.summarization.SummarizationScreen

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

        composable(
            route = SummarizationDestination.route,
            arguments = listOf(
                navArgument(SummarizationDestination.argName) {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = springAnimationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = springAnimationSpec
                )
            },
        ) {
            SummarizationScreen(navController = navController)
        }

        composable(
            route = ScannerDestination.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = springAnimationSpec
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = springAnimationSpec
                )
            },
        ) {
            ScannerScreen(navController = navController)
        }
    }
}