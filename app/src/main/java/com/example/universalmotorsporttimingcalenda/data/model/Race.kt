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
)
