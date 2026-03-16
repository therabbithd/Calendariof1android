package com.example.universalmotorsporttimingcalenda.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.universalmotorsporttimingcalenda.ui.detail.F1RaceDetailScreen
import com.example.universalmotorsporttimingcalenda.ui.list.F1RaceListScreen
import com.example.universalmotorsporttimingcalenda.ui.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String) {
    @Serializable
    data object Home : Route("home")

    @Serializable
    data object List : Route("race_list")

    @Serializable
    data class Detail(val id: Long) : Route(route = "race_detail[$id]")

    @Serializable
    data object Profile : Route("profile")

    @Serializable
    data object Login : Route("login")

    @Serializable
    data object Register : Route("register")

    @Serializable
    data object CreateProfile : Route("create_profile")

    @Serializable
    data object EditProfile : Route("edit_profile")

    @Serializable
    data class Camera(val round: Int) : Route(route = "camera[$round]")

    @Serializable
    data object Calendar : Route("calendar")
}

fun NavController.navigateToHome() {
    this.navigate(Route.Home) {
        popUpTo(Route.Home) { inclusive = true }
    }
}

fun NavController.navigateToRaceList() {
    this.navigate(Route.List)
}

fun NavController.navigateToRaceDetails(round: Int) {
    this.navigate(Route.Detail(round.toLong()))
}

fun NavController.navigateToProfile() {
    this.navigate(Route.Profile)
}

fun NavController.navigateToCamera(round: Int) {
    this.navigate(Route.Camera(round))
}

fun NavController.navigateToLogin() {
    this.navigate(Route.Login)
}

fun NavController.navigateToRegister() {
    this.navigate(Route.Register)
}

fun NavController.navigateToCreateProfile() {
    this.navigate(Route.CreateProfile) {
        popUpTo(Route.Register) { inclusive = true }
        popUpTo(Route.Login) { inclusive = true }
    }
}

fun NavController.navigateToEditProfile() {
    this.navigate(Route.EditProfile)
}

fun NavController.navigateToCalendar() {
    this.navigate(Route.Calendar)
}

fun NavGraphBuilder.raceDetailDestination(
    modifier: Modifier = Modifier,
    onNavigateToCamera: (Int) -> Unit = {},
    onNavigateBack: () -> Unit
) {
    composable<Route.Detail> {
        F1RaceDetailScreen(
            modifier = modifier,
            onNavigateToCamera = onNavigateToCamera,
            onNavigateBack = onNavigateBack
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
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    composable<Route.Login> {
        com.example.universalmotorsporttimingcalenda.ui.auth.LoginScreen(
            modifier = modifier,
            onLoginSuccess = onLoginSuccess,
            onNavigateToRegister = onNavigateToRegister
        )
    }
}

fun NavGraphBuilder.registerDestination(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    composable<Route.Register> {
        com.example.universalmotorsporttimingcalenda.ui.auth.RegisterScreen(
            modifier = modifier,
            onRegisterSuccess = onRegisterSuccess,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavGraphBuilder.createProfileDestination(
    modifier: Modifier = Modifier,
    onCreateSuccess: () -> Unit,
    onSkip: () -> Unit
) {
    composable<Route.CreateProfile> {
        com.example.universalmotorsporttimingcalenda.ui.auth.CreateProfileScreen(
            modifier = modifier,
            onCreateSuccess = onCreateSuccess,
            onSkip = onSkip
        )
    }
}

fun NavGraphBuilder.profileDestination(
    modifier: Modifier = Modifier,
    onNavigateToEdit: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    composable<Route.Profile> {
        com.example.universalmotorsporttimingcalenda.ui.auth.ProfileScreen(
            modifier = modifier,
            onNavigateToEdit = onNavigateToEdit,
            onNavigateToCreate = onNavigateToCreate
        )
    }
}

fun NavGraphBuilder.editProfileDestination(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onUpdateSuccess: () -> Unit
) {
    this.composable<Route.EditProfile> {
        com.example.universalmotorsporttimingcalenda.ui.auth.EditProfileScreen(
            modifier = modifier,
            onNavigateBack = onNavigateBack,
            onUpdateSuccess = onUpdateSuccess
        )
    }
}

fun NavGraphBuilder.calendarDestination(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (Int) -> Unit
) {
    this.composable<Route.Calendar> {
        com.example.universalmotorsporttimingcalenda.ui.calendar.CalendarScreen(
            modifier = modifier,
            onNavigateToDetails = onNavigateToDetails
        )
    }
}
