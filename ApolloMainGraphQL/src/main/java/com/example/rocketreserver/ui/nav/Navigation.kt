package com.example.rocketreserver.ui.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rocketreserver.ui.screens.LaunchDetails
import com.example.rocketreserver.ui.screens.LaunchList
import com.example.rocketreserver.ui.screens.Login

object NavigationDestinations {
    const val LAUNCH_LIST = "launchList"
    const val LAUNCH_DETAILS = "launchDetails"
    const val LOGIN = "login"
}

object NavigationArguments {
    const val LAUNCH_ID = "launchId"
}


@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationDestinations.LAUNCH_LIST) {
        composable(route = NavigationDestinations.LAUNCH_LIST) {
            LaunchList(
                onLaunchClick = { launchId ->
                    navController.navigate("${NavigationDestinations.LAUNCH_DETAILS}/$launchId")
                }
            )
        }

        composable(route = "${NavigationDestinations.LAUNCH_DETAILS}/{${NavigationArguments.LAUNCH_ID}}") { navBackStackEntry ->
            LaunchDetails(launchId = navBackStackEntry.arguments!!.getString(NavigationArguments.LAUNCH_ID)!!,
                navigateToLogin = {
                    Log.e("TAG", "navigateToLogin: ")
                    navController.navigate(NavigationDestinations.LOGIN)
                }
            )
        }

        composable(route = NavigationDestinations.LOGIN) {
            Login(
                navigateBack = {
                    navController.popBackStack()

                })
        }
    }
}
