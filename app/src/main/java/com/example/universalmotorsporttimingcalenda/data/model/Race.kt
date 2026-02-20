package com.example.universalmotorsporttimingcalenda.data.model

data class Race(
    val season: String,
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val locality: String,
    val country: String,
    val date: String,
    val time: String?,
    val firstPractice: Session?,
    val secondPractice: Session?,
    val thirdPractice: Session?,
    val qualifying: Session?,
    val sprint: Session?,
    val sprintQualifying: Session?,
    val lat: String,
    val long: String
)

data class Session(
    val date: String,
    val time: String
)
