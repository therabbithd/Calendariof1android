package com.example.universalmotorsporttimingcalenda.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun F1RaceListScreen(
    modifier: Modifier = Modifier,
    viewModel: F1RaceListViewModel = hiltViewModel(),
    onShowDetail: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is RaceListUiState.Initial -> Unit
        is RaceListUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingIndicator(modifier = Modifier.size(128.dp))
            }
        }
        is RaceListUiState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Se ha producido un error", style = MaterialTheme.typography.titleLarge)
            }
        }
        is RaceListUiState.Success -> {
            val state = uiState as RaceListUiState.Success
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = state.races,
                    key = { item -> item.round }
                ) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onShowDetail(item.round) }
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Ronda ${item.round}: ${item.raceName}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = item.circuit)
                            Text(text = item.country)
                            Text(text = item.date)
                        }
                    }
                }
            }
        }
    }
}
