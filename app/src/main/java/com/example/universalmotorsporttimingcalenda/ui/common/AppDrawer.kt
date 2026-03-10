package com.example.universalmotorsporttimingcalenda.ui.common

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.res.stringResource
import com.example.universalmotorsporttimingcalenda.R
import com.example.universalmotorsporttimingcalenda.ui.navigation.Route

@Composable
fun AppDrawer(
    userName: String?,
    userEmail: String?,
    userAvatar: String?,
    currentRoute: String?,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToProfile: () -> Unit,
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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userAvatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.profile_picture_description),
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_background)
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

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.races)) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = currentRoute == Route.List.route,
            onClick = {
                navigateToHome()
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
            label = { Text(stringResource(id = R.string.login)) },
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
