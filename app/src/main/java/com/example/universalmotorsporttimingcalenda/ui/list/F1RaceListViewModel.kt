package com.example.universalmotorsporttimingcalenda.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.model.Race
import com.example.universalmotorsporttimingcalenda.data.repository.F1Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class F1RaceListViewModel @Inject constructor(
    private val repository: F1Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RaceListUiState> =
        MutableStateFlow(RaceListUiState.Initial)
    val uiState: StateFlow<RaceListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = RaceListUiState.Loading

            repository.observeRaces().collect { result ->
                if (result.isSuccess) {
                    val races = result.getOrNull()!!
                    val uiRaces = races.asListUiState()
                    _uiState.value = RaceListUiState.Success(uiRaces)
                } else {
                    _uiState.value = RaceListUiState.Error
                }
            }
        }
    }
}

sealed class RaceListUiState {
    object Initial : RaceListUiState()
    object Loading : RaceListUiState()
    object Error : RaceListUiState()
    data class Success(
        val races: List<RaceListItemUiState>
    ) : RaceListUiState()
}

data class RaceListItemUiState(
    val round: Int,
    val raceName: String,
    val circuit: String,
    val locality: String,
    val country: String,
    val date: String,
    val time: String?
)

fun Race.asListItemUiState(): RaceListItemUiState {
    return RaceListItemUiState(
        round = this.round,
        raceName = this.raceName,
        circuit = this.circuitName,
        locality = this.locality,
        country = this.country,
        date = this.date,
        time = this.time
    )
}

fun List<Race>.asListUiState(): List<RaceListItemUiState> =
    this.map(Race::asListItemUiState)
