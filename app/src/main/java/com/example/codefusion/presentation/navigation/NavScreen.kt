package com.example.codefusion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codefusion.presentation.navigation.NavigationRoutes.DETAIL
import com.example.codefusion.presentation.navigation.NavigationRoutes.HOME
import com.example.codefusion.presentation.ui.screens.detail.UserDetailScreen
import com.example.codefusion.presentation.ui.screens.listing.UserListScreen

@Composable
fun NavScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME) {
        composable(HOME,) { UserListScreen(
                                     onUserClick={ navController.navigate(DETAIL+"/${it.id}") },
                                     onBackClick = { navController.popBackStack() }
                                    )
        }

        composable(
            route = "$DETAIL/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserDetailScreen( onBackClick = { navController.popBackStack() }
                ,userId = userId) }
    }
}