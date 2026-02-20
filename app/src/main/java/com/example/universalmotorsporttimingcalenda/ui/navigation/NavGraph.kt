package com.example.universalmotorsporttimingcalenda.ui.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.universalmotorsporttimingcalenda.ui.common.AppDrawer
import com.example.universalmotorsporttimingcalenda.ui.common.AppTopBar
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(sessionManager: SessionManager) {
    val navController = rememberNavController()
    val startDestination = Route.List
    val backStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = sessionManager.userName,
                userEmail = sessionManager.userEmail,
                userAvatar = sessionManager.userAvatar,
                currentRoute = currentRoute,
                navigateToHome = {
                    navController.navigate(Route.List) {
                        popUpTo(Route.List) { inclusive = true }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Route.Login)
                },
                navigateToProfile = {
                    navController.navigate(Route.Profile)
                },
                closeDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    backStackEntry = backStackEntry,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        ) { innerPadding ->
            val contentModifier =
                Modifier.consumeWindowInsets(innerPadding).padding(innerPadding)

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                raceListDestination(
                    contentModifier,
                    onNavigateToDetails = {
                        navController.navigateToRaceDetails(it)
                    }
                )
                raceDetailDestination(contentModifier)
                loginDestination(
                    contentModifier,
                    onLoginSuccess = {
                        navController.navigate(Route.Profile) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                )
                profileDestination(contentModifier)
            }
        }
    }
}
