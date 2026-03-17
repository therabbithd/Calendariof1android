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
import android.content.Context
import com.example.universalmotorsporttimingcalenda.util.NotificationScheduler
import com.example.universalmotorsporttimingcalenda.util.FlagMapping
import javax.inject.Inject

import com.example.universalmotorsporttimingcalenda.data.model.Session
import com.example.universalmotorsporttimingcalenda.data.repository.SessionWeather
import com.example.universalmotorsporttimingcalenda.data.repository.WeatherRepository
import com.example.universalmotorsporttimingcalenda.data.remote.WeatherResponse

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
    val sprintQualifying: Session? = null,
    val raceSession: Session? = null,
    val lat: String = "",
    val long: String = "",
    val flagUrl: String = "",
    val firstPracticeWeather: SessionWeather? = null,
    val secondPracticeWeather: SessionWeather? = null,
    val thirdPracticeWeather: SessionWeather? = null,
    val qualifyingWeather: SessionWeather? = null,
    val sprintWeather: SessionWeather? = null,
    val sprintQualifyingWeather: SessionWeather? = null,
    val raceSessionWeather: SessionWeather? = null
)

@HiltViewModel
class F1RaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: F1Repository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RaceDetailUiState> =
        MutableStateFlow(RaceDetailUiState())
    val uiState: StateFlow<RaceDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val route = savedStateHandle.toRoute<Route.Detail>()
            val round = route.id.toInt()
            repository.observeOneRace(round).collect { result ->
                val race = result.getOrNull()
                race?.let { r ->
                    _uiState.value = r.toDetailUiState()
                    fetchWeather(r)
                }
            }
        }
    }

    private fun fetchWeather(race: Race) {
        viewModelScope.launch {
            val sessions = listOfNotNull(
                race.firstPractice,
                race.secondPractice,
                race.thirdPractice,
                race.qualifying,
                race.sprint,
                race.sprintQualifying,
                race.time?.let { Session(race.date, it) }
            )
            
            if (sessions.isEmpty()) return@launch

            val sortedDates = sessions.map { it.date }.sorted()
            val startDate = sortedDates.first()
            val endDate = sortedDates.last()

            val result = weatherRepository.getWeatherData(
                lat = race.lat.toDoubleOrNull() ?: 0.0,
                lon = race.long.toDoubleOrNull() ?: 0.0,
                startDate = startDate,
                endDate = endDate
            )
            val weatherResponse = result.getOrNull() ?: return@launch
            
            _uiState.value = _uiState.value.copy(
                firstPracticeWeather = race.firstPractice?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                secondPracticeWeather = race.secondPractice?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                thirdPracticeWeather = race.thirdPractice?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                qualifyingWeather = race.qualifying?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                sprintWeather = race.sprint?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                sprintQualifyingWeather = race.sprintQualifying?.let { weatherRepository.getSessionWeather(weatherResponse, it.date, it.time) },
                raceSessionWeather = race.time?.let { weatherRepository.getSessionWeather(weatherResponse, race.date, it) }
            )
        }
    }

    fun scheduleTestNotification(context: Context) {
        val scheduler = NotificationScheduler(context)
        val raceName = _uiState.value.raceName
        scheduler.scheduleTestNotification(
            title = "F1 Test Notification",
            message = "This is a test notification for $raceName"
        )
    }
}

fun Race.toDetailUiState(): RaceDetailUiState {
    val (formattedRaceDate, formattedRaceTime) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(this.date, this.time)

    return RaceDetailUiState(
        round = this.round,
        raceName = this.raceName,
        circuit = this.circuitName,
        locality = this.locality,
        country = this.country,
        date = formattedRaceDate,
        time = formattedRaceTime,
        firstPractice = this.firstPractice?.let {
             val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
             Session(d, t)
        },
        secondPractice = this.secondPractice?.let {
            val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
            Session(d, t)
        },
        thirdPractice = this.thirdPractice?.let {
            val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
            Session(d, t)
        },
        qualifying = this.qualifying?.let {
            val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
            Session(d, t)
        },
        sprint = this.sprint?.let {
            val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
            Session(d, t)
        },
        sprintQualifying = this.sprintQualifying?.let {
            val (d, t) = com.example.universalmotorsporttimingcalenda.util.DateUtils.formatToLocalTime(it.date, it.time)
            Session(d, t)
        },
        raceSession = Session(formattedRaceDate, formattedRaceTime),
        lat = this.lat,
        long = this.long,
        flagUrl = FlagMapping.getFlagUrl(this.country)
    )
}
