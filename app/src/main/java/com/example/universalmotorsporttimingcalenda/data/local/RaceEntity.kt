package com.example.universalmotorsporttimingcalenda.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.universalmotorsporttimingcalenda.data.model.Race
import com.example.universalmotorsporttimingcalenda.data.model.Session

@Entity(tableName = "races")
data class RaceEntity(
    val season: String,
    @PrimaryKey
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val locality: String,
    val country: String,
    val date: String,
    val time: String?,
    @Embedded(prefix = "fp1_")
    val firstPractice: SessionEntity?,
    @Embedded(prefix = "fp2_")
    val secondPractice: SessionEntity?,
    @Embedded(prefix = "fp3_")
    val thirdPractice: SessionEntity?,
    @Embedded(prefix = "qual_")
    val qualifying: SessionEntity?,
    @Embedded(prefix = "sprint_")
    val sprint: SessionEntity?,
    @Embedded(prefix = "sprint_qual_")
    val sprintQualifying: SessionEntity?,
    val lat: String,
    val long: String
)

data class SessionEntity(
    val date: String,
    val time: String
)

fun Session.toEntity(): SessionEntity = SessionEntity(date = date, time = time)
fun SessionEntity.toModel(): Session = Session(date = date, time = time)

fun Race.toEntity(): RaceEntity = RaceEntity(
    season = season,
    round = round,
    raceName = raceName,
    circuitName = circuitName,
    locality = locality,
    country = country,
    date = date,
    time = time,
    firstPractice = firstPractice?.toEntity(),
    secondPractice = secondPractice?.toEntity(),
    thirdPractice = thirdPractice?.toEntity(),
    qualifying = qualifying?.toEntity(),
    sprint = sprint?.toEntity(),
    sprintQualifying = sprintQualifying?.toEntity(),
    lat = lat,
    long = long
)

fun RaceEntity.toModel(): Race = Race(
    season = season,
    round = round,
    raceName = raceName,
    circuitName = circuitName,
    locality = locality,
    country = country,
    date = date,
    time = time,
    firstPractice = firstPractice?.toModel(),
    secondPractice = secondPractice?.toModel(),
    thirdPractice = thirdPractice?.toModel(),
    qualifying = qualifying?.toModel(),
    sprint = sprint?.toModel(),
    sprintQualifying = sprintQualifying?.toModel(),
    lat = lat,
    long = long
)

fun List<RaceEntity>.toModel(): List<Race> = map { it.toModel() }
