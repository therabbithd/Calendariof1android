package com.example.universalmotorsporttimingcalenda.data.local

class RaceNotFoundException(round: Int) : RuntimeException("Race with round $round not found")
