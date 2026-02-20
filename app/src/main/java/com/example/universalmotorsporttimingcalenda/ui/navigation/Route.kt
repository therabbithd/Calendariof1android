package com.example.universalmotorsporttimingcalenda.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.universalmotorsporttimingcalenda.ui.detail.F1RaceDetailScreen
import com.example.universalmotorsporttimingcalenda.ui.list.F1RaceListScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String) {
    @Serializable
    data object List : Route("race_list")

    @Serializable
    data class Detail(val id: Long) : Route(route = "race_detail[$id]")

    @Serializable
    data object Profile : Route("profile")

    @Serializable
    data object Login : Route("login")
}

fun NavController.navigateToRaceDetails(round: Int) {
    this.navigate(Route.Detail(round.toLong()))
}

fun NavController.navigateToProfile() {
    this.navigate(Route.Profile)
}

fun NavGraphBuilder.raceDetailDestination(
    modifier: Modifier = Modifier,
) {
    composable<Route.Detail> {
        F1RaceDetailScreen(
            modifier = modifier,
        )
    }
}

fun NavGraphBuilder.raceListDestination(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (Int) -> Unit
) {
    composable<Route.List> {
        F1RaceListScreen(
            modifier = modifier,
            onShowDetail = { round ->
                onNavigateToDetails(round)
            }
        )
    }
}

fun NavGraphBuilder.loginDestination(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {
    composable<Route.Login> {
        com.example.universalmotorsporttimingcalenda.ui.auth.LoginScreen(
            modifier = modifier,
            onLoginSuccess = onLoginSuccess
        )
    }
}

fun NavGraphBuilder.profileDestination(
    modifier: Modifier = Modifier,
) {
    composable<Route.Profile> {
        com.example.universalmotorsporttimingcalenda.ui.auth.ProfileScreen(
            modifier = modifier,
        )
    }
}
