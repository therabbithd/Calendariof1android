package com.example.universalmotorsporttimingcalenda.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.universalmotorsporttimingcalenda.R

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
            )
            
            Text(
                text = stringResource(id = R.string.home_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    if (isLoggedIn) {
                        onLogout()
                    } else {
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = if (isLoggedIn) {
                        stringResource(id = R.string.logout)
                    } else {
                        stringResource(id = R.string.login_button_label)
                    }
                )
            }

            OutlinedButton(
                onClick = onNavigateToCalendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.calendar_button_label))
            }
        }
    }
}
