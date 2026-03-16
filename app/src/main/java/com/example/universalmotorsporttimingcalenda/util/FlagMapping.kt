package com.example.universalmotorsporttimingcalenda.util

object FlagMapping {
    private val mapping = mapOf(
        "Australia" to "au",
        "Bahrain" to "bh",
        "Saudi Arabia" to "sa",
        "Japan" to "jp",
        "China" to "cn",
        "USA" to "us",
        "United States" to "us",
        "Italy" to "it",
        "Monaco" to "mc",
        "Canada" to "ca",
        "Spain" to "es",
        "Austria" to "at",
        "UK" to "gb",
        "United Kingdom" to "gb",
        "Hungary" to "hu",
        "Belgium" to "be",
        "Netherlands" to "nl",
        "Azerbaijan" to "az",
        "Singapore" to "sg",
        "Mexico" to "mx",
        "Brazil" to "br",
        "Qatar" to "qa",
        "UAE" to "ae"
    )

    fun getFlagUrl(country: String): String {
        val code = mapping[country] ?: "un" // 'un' for unknown or fallback
        return "https://flagcdn.com/w80/${code.lowercase()}.png"
    }
}
