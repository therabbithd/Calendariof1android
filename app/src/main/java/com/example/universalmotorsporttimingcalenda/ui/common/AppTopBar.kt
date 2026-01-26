package com.example.universalmotorsporttimingcalenda.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    backStackEntry: NavBackStackEntry? = null,
) {
    TopAppBar(
        title = {
            Text(text = "Universal Motorsport Timing Calendar")
        }
    )
}
