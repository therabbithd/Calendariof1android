package com.example.universalmotorsporttimingcalenda.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    fun formatToLocalTime(dateStr: String, timeStr: String?): Pair<String, String> {
        return try {
            // Check if time is available
            if (timeStr.isNullOrBlank()) {
                // If no time, just format the date locally or return as is?
                // Usually race dates are consistent, but let's try to parse if possible, 
                // or just return the date string and a placeholder for time.
                // For safety, let's just return the original date and empty time if parsing fails or time is missing.
                 return Pair(dateStr, "")
            }

            // Combine date and time (e.g. 2023-11-26 and 13:00:00Z)
            // Ergast time usually ends with 'Z' for UTC.
            // Example input: date="2023-11-26", time="13:00:00Z"
            
            // Clean specific Ergast format oddities if any, but standard ISO is expected.
            val cleanTime = if (timeStr.endsWith("Z")) timeStr else "${timeStr}Z"
            val dateTimeStr = "${dateStr}T$cleanTime" 

            // Format: 2023-11-26T13:00:00Z
            // We use a format that handles the 'Z' correctly (X for ISO 8601 in newer Java, but for minSdk 24 'Z' literals might need handling or just 'yyyy-MM-dd\'T\'HH:mm:ss\'Z\'')
            // Actually, simplest is to use "yyyy-MM-dd'T'HH:mm:ss'Z'" and set timezone to UTC.
            
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            
            val date: Date = parser.parse(dateTimeStr) ?: return Pair(dateStr, timeStr)

            // Format to local
            val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getDefault()
            
            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormatter.timeZone = TimeZone.getDefault()

            Pair(dateFormatter.format(date), timeFormatter.format(date))

        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to original strings if parsing fails
            Pair(dateStr, timeStr ?: "")
        }
    }
}
