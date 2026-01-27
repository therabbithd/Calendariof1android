package com.example.universalmotorsporttimingcalenda.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalmotorsporttimingcalenda.ui.navigation.Route

@Composable
fun AppDrawer(
    currentRoute: String?,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        NavigationDrawerItem(
            label = { Text("Races") },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = currentRoute == Route.List.route,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            modifier = Modifier.padding(top = 16.dp, start = 12.dp, end = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Login") },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            selected = currentRoute == Route.Login.route,
            onClick = {
                navigateToLogin()
                closeDrawer()
            },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
    }
}
