package com.example.universalmotorsporttimingcalenda.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun F1RaceDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: F1RaceDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Ronda ${uiState.round}: ${uiState.raceName}",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = uiState.circuit, style = MaterialTheme.typography.titleMedium)
        Text(text = "${uiState.locality} (${uiState.country})")
        Text(text = "Fecha: ${uiState.date}")
        uiState.time?.let { Text(text = "Hora: $it") }
    }
}
