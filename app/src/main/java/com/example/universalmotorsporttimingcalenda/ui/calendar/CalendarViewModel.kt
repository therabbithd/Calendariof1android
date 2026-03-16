package com.example.universalmotorsporttimingcalenda.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.model.Race
import com.example.universalmotorsporttimingcalenda.data.repository.F1Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: F1Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadRaces()
    }

    private fun loadRaces() {
        viewModelScope.launch {
            repository.observeRaces().collect { result ->
                if (result.isSuccess) {
                    val races = result.getOrNull() ?: emptyList()
                    val sessionsByDate = groupSessionsByDate(races)
                    _uiState.value = _uiState.value.copy(
                        sessionsByDate = sessionsByDate,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    private fun groupSessionsByDate(races: List<Race>): Map<LocalDate, List<CalendarSession>> {
        val sessions = mutableMapOf<LocalDate, MutableList<CalendarSession>>()

        races.forEach { race ->
            // Race session
            addSession(sessions, race.date, race.raceName, "Race", race.round)
            
            // Practice sessions
            race.firstPractice?.let { addSession(sessions, it.date, race.raceName, "Practice 1", race.round) }
            race.secondPractice?.let { addSession(sessions, it.date, race.raceName, "Practice 2", race.round) }
            race.thirdPractice?.let { addSession(sessions, it.date, race.raceName, "Practice 3", race.round) }
            
            // Qualifying
            race.qualifying?.let { addSession(sessions, it.date, race.raceName, "Qualifying", race.round) }
            
            // Sprint sessions
            race.sprint?.let { addSession(sessions, it.date, race.raceName, "Sprint", race.round) }
            race.sprintQualifying?.let { addSession(sessions, it.date, race.raceName, "Sprint Qualifying", race.round) }
        }

        return sessions
    }

    private fun addSession(
        sessions: MutableMap<LocalDate, MutableList<CalendarSession>>,
        dateStr: String,
        raceName: String,
        sessionType: String,
        round: Int
    ) {
        try {
            val date = LocalDate.parse(dateStr)
            val sessionList = sessions.getOrPut(date) { mutableListOf() }
            sessionList.add(CalendarSession(raceName, sessionType, round))
        } catch (e: Exception) {
            // Ignore malformed dates
        }
    }

    fun onMonthChange(month: YearMonth) {
        _uiState.value = _uiState.value.copy(selectedMonth = month)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }
}

data class CalendarUiState(
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val sessionsByDate: Map<LocalDate, List<CalendarSession>> = emptyMap(),
    val isLoading: Boolean = true
)

data class CalendarSession(
    val raceName: String,
    val type: String,
    val round: Int
)
