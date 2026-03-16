package com.example.universalmotorsporttimingcalenda.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.universalmotorsporttimingcalenda.R
import com.example.universalmotorsporttimingcalenda.ui.navigation.Route

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToRaces: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val isHomeSelected = currentRoute == Route.Home.route
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.home)) },
            selected = isHomeSelected,
            onClick = onNavigateToHome,
            alwaysShowLabel = true
        )

        val isCalendarSelected = currentRoute == Route.Calendar.route
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isCalendarSelected) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.calendar_button_label)) },
            selected = isCalendarSelected,
            onClick = onNavigateToCalendar,
            alwaysShowLabel = true
        )

        val isRaceListSelected = currentRoute == Route.List.route
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isRaceListSelected) Icons.Filled.List else Icons.Outlined.List,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.races)) },
            selected = isRaceListSelected,
            onClick = onNavigateToRaces,
            alwaysShowLabel = true
        )

        val isProfileSelected = currentRoute == Route.Profile.route
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isProfileSelected) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.profile)) },
            selected = isProfileSelected,
            onClick = onNavigateToProfile,
            alwaysShowLabel = true
        )
    }
}
