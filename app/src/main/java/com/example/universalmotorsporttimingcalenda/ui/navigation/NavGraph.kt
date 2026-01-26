package com.example.universalmotorsporttimingcalenda.ui.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.universalmotorsporttimingcalenda.ui.common.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val startDestination = Route.List
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(backStackEntry)
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
        }
    }
}
