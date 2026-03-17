package com.example.universalmotorsporttimingcalenda.ui.common

import android.app.Activity
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.example.universalmotorsporttimingcalenda.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    backStackEntry: NavBackStackEntry? = null,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current
    var showLanguageMenu by remember { mutableStateOf(false) }

    fun switchLocale(languageCode: String) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putString("locale", languageCode)
            .apply()
        (context as? Activity)?.recreate()
    }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_bar_title),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.menu_description)
                )
            }
        },
        actions = {
            IconButton(onClick = { showLanguageMenu = true }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(id = R.string.select_language)
                )
            }
            DropdownMenu(
                expanded = showLanguageMenu,
                onDismissRequest = { showLanguageMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.english)) },
                    onClick = {
                        showLanguageMenu = false
                        switchLocale("en")
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.spanish)) },
                    onClick = {
                        showLanguageMenu = false
                        switchLocale("es")
                    }
                )
            }
        }
    )
}

