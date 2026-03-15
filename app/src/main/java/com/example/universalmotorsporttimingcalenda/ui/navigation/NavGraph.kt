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
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.universalmotorsporttimingcalenda.ui.common.AppDrawer
import com.example.universalmotorsporttimingcalenda.ui.common.AppTopBar
import com.example.universalmotorsporttimingcalenda.ui.camera.CameraScreen
import com.example.universalmotorsporttimingcalenda.ui.home.HomeScreen
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(sessionManager: SessionManager) {
    val navController = rememberNavController()
    val startDestination = Route.Home
    val backStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = backStackEntry?.destination?.route

    val token by sessionManager.token.collectAsStateWithLifecycle(initialValue = null)
    val userName by sessionManager.userName.collectAsStateWithLifecycle(initialValue = null)
    val userEmail by sessionManager.userEmail.collectAsStateWithLifecycle(initialValue = null)
    val userAvatar by sessionManager.userAvatar.collectAsStateWithLifecycle(initialValue = null)
    val isLoggedIn = token != null

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = userName,
                userEmail = userEmail,
                userAvatar = userAvatar,
                currentRoute = currentRoute,
                isLoggedIn = isLoggedIn,
                onLogout = {
                    scope.launch {
                        sessionManager.clear()
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Home) { inclusive = true }
                        }
                    }
                },
                navigateToHome = {
                    navController.navigateToHome()
                },
                navigateToRaces = {
                    if (isLoggedIn) {
                        navController.navigateToRaceList()
                    } else {
                        navController.navigateToLogin()
                    }
                },
                navigateToLogin = {
                    navController.navigateToLogin()
                },
                navigateToProfile = {
                    navController.navigateToProfile()
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
                composable<Route.Home> {
                    HomeScreen(
                        onNavigateToLogin = { navController.navigateToLogin() },
                        onNavigateToCalendar = {
                            if (isLoggedIn) {
                                navController.navigateToRaceList()
                            } else {
                                navController.navigateToLogin()
                            }
                        },
                        isLoggedIn = isLoggedIn,
                        onLogout = {
                            scope.launch {
                                sessionManager.clear()
                                navController.navigate(Route.Home) {
                                    popUpTo(Route.Home) { inclusive = true }
                                }
                            }
                        },
                        modifier = contentModifier
                    )
                }
                raceListDestination(
                    contentModifier,
                    onNavigateToDetails = { round ->
                        if (isLoggedIn) {
                            navController.navigateToRaceDetails(round)
                        } else {
                            navController.navigateToLogin()
                        }
                    }
                )
                raceDetailDestination(
                    modifier = contentModifier,
                    onNavigateToCamera = { round ->
                        navController.navigateToCamera(round)
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
                loginDestination(
                    modifier = contentModifier,
                    onLoginSuccess = {
                        navController.navigate(Route.Profile) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigateToRegister()
                    }
                )
                registerDestination(
                    modifier = contentModifier,
                    onRegisterSuccess = {
                        navController.navigateToCreateProfile()
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
                createProfileDestination(
                    modifier = contentModifier,
                    onCreateSuccess = {
                        navController.navigateToProfile()
                    },
                    onSkip = {
                        navController.navigateToProfile()
                    }
                )
                profileDestination(contentModifier)
                composable<Route.Camera> { backStackEntry ->
                    val cameraRoute = backStackEntry.toRoute<Route.Camera>()
                    CameraScreen(
                        round = cameraRoute.round,
                        onPhotoTaken = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
