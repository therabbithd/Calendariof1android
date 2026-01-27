package com.example.universalmotorsporttimingcalenda.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.universalmotorsporttimingcalenda.data.model.Race
import com.example.universalmotorsporttimingcalenda.data.repository.F1Repository
import com.example.universalmotorsporttimingcalenda.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.universalmotorsporttimingcalenda.data.model.Session

data class RaceDetailUiState(
    val round: Int = 0,
    val raceName: String = "",
    val circuit: String = "",
    val locality: String = "",
    val country: String = "",
    val date: String = "",
    val time: String? = "",
    val firstPractice: Session? = null,
    val secondPractice: Session? = null,
    val thirdPractice: Session? = null,
    val qualifying: Session? = null,
    val sprint: Session? = null,
    val sprintQualifying: Session? = null
)

@HiltViewModel
class F1RaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: F1Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RaceDetailUiState> =
        MutableStateFlow(RaceDetailUiState())
    val uiState: StateFlow<RaceDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val route = savedStateHandle.toRoute<Route.Detail>()
            val round = route.id.toInt()
            val raceResult = repository.readOneRace(round)
            val race = raceResult.getOrNull()
            race?.let {
                _uiState.value = it.toDetailUiState()
            }
        }
    }
}

fun Race.toDetailUiState(): RaceDetailUiState = RaceDetailUiState(
    round = this.round,
    raceName = this.raceName,
    circuit = this.circuitName,
    locality = this.locality,
    country = this.country,
    date = this.date,
    time = this.time,
    firstPractice = this.firstPractice,
    secondPractice = this.secondPractice,
    thirdPractice = this.thirdPractice,
    qualifying = this.qualifying,
    sprint = this.sprint,
    sprintQualifying = this.sprintQualifying
)
