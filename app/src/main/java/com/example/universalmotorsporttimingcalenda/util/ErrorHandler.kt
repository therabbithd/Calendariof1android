package com.example.universalmotorsporttimingcalenda.util

import com.google.gson.Gson
import com.google.gson.JsonObject

object ErrorHandler {
    private val gson = Gson()

    fun parseError(errorMessage: String): String {
        return try {
            val jsonObject = gson.fromJson(errorMessage, JsonObject::class.java)
            
            // Check for structure {"message": "..."}
            if (jsonObject.has("message")) {
                return jsonObject.get("message").asString
            }

            // Check if it's the specific structure {"errors": {"formErrors": [], "fieldErrors": {...}}}
            if (jsonObject.has("errors")) {
                val errors = jsonObject.getAsJsonObject("errors")
                val result = mutableListOf<String>()

                // Handle formErrors
                if (errors.has("formErrors")) {
                    val formErrors = errors.getAsJsonArray("formErrors")
                    formErrors.forEach { result.add(it.asString) }
                }

                // Handle fieldErrors
                if (errors.has("fieldErrors")) {
                    val fieldErrors = errors.getAsJsonObject("fieldErrors")
                    fieldErrors.entrySet().forEach { entry ->
                        val fieldName = translateFieldName(entry.key)
                        val messages = entry.value.asJsonArray
                        messages.forEach { msg ->
                            result.add("$fieldName: ${msg.asString}")
                        }
                    }
                }

                if (result.isNotEmpty()) {
                    return result.joinToString("\n")
                }
            }
            
            // Fallback for other simple JSON errors or plain text
            errorMessage
        } catch (e: Exception) {
            // If it's not valid JSON, return as is
            errorMessage
        }
    }

    private fun translateFieldName(field: String): String {
        return when (field.lowercase()) {
            "email" -> "Correo electrónico"
            "password" -> "Contraseña"
            "name" -> "Nombre"
            "avatar" -> "Imagen de perfil"
            "bio" -> "Biografía"
            "phone" -> "Teléfono"
            "address" -> "Dirección"
            else -> field.replaceFirstChar { it.uppercase() }
        }
    }
}
