package com.example.universalmotorsporttimingcalenda.data.remote.model

import com.google.gson.annotations.SerializedName
import com.example.universalmotorsporttimingcalenda.data.model.Race

data class ErgastResponse(
    @SerializedName("MRData") val mrData: MRData
)

data class MRData(
    val series: String,
    val url: String,
    val limit: String,
    val offset: String,
    val total: String,
    @SerializedName("RaceTable") val raceTable: RaceTable
)

data class RaceTable(
    val season: String,
    @SerializedName("Races") val races: List<RaceRemote>
)

data class RaceRemote(
    val season: String,
    val round: String,
    val url: String,
    val raceName: String,
    @SerializedName("Circuit") val circuit: CircuitRemote,
    val date: String,
    val time: String?,
    @SerializedName("FirstPractice") val firstPractice: SessionRemote?,
    @SerializedName("SecondPractice") val secondPractice: SessionRemote?,
    @SerializedName("ThirdPractice") val thirdPractice: SessionRemote?,
    @SerializedName("Qualifying") val qualifying: SessionRemote?,
    @SerializedName("Sprint") val sprint: SessionRemote?,
    @SerializedName("SprintQualifying") val sprintQualifying: SessionRemote?
)

data class CircuitRemote(
    val circuitId: String,
    val url: String,
    val circuitName: String,
    @SerializedName("Location") val location: LocationRemote
)

data class LocationRemote(
    val lat: String,
    @SerializedName("long") val long: String,
    val locality: String,
    val country: String
)

data class SessionRemote(
    val date: String,
    val time: String
)

fun RaceRemote.toExternal(): Race {
    return Race(
        season = season,
        round = round.toIntOrNull() ?: 0,
        raceName = raceName,
        circuitName = circuit.circuitName,
        locality = circuit.location.locality,
        country = circuit.location.country,
        date = date,
        time = time
    )
}
