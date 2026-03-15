package com.example.universalmotorsporttimingcalenda.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.universalmotorsporttimingcalenda.R
import com.example.universalmotorsporttimingcalenda.ui.navigation.Route

@Composable
fun AppDrawer(
    userName: String?,
    userEmail: String?,
    userAvatar: String?, // Kept for signature compatibility
    currentRoute: String?,
    navigateToHome: () -> Unit,
    navigateToRaces: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    isLoggedIn: Boolean,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userName ?: stringResource(id = R.string.guest),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = userEmail ?: stringResource(id = R.string.not_logged_in),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // "Home" item removed as per request

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.races)) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = currentRoute == Route.List.route,
            onClick = {
                navigateToRaces()
                closeDrawer()
            },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.profile)) },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            selected = currentRoute == Route.Profile.route,
            onClick = {
                navigateToProfile()
                closeDrawer()
            },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = if (isLoggedIn) {
                        stringResource(id = R.string.logout)
                    } else {
                        stringResource(id = R.string.login)
                    }
                )
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            selected = !isLoggedIn && currentRoute == Route.Login.route,
            onClick = {
                if (isLoggedIn) {
                    onLogout()
                } else {
                    navigateToLogin()
                }
                closeDrawer()
            },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
    }
}
