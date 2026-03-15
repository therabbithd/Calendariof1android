package com.example.universalmotorsporttimingcalenda.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_AVATAR_KEY = stringPreferencesKey("user_avatar")

    val token: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }
    val userName: Flow<String?> = dataStore.data.map { it[USER_NAME_KEY] }
    val userEmail: Flow<String?> = dataStore.data.map { it[USER_EMAIL_KEY] }
    val userAvatar: Flow<String?> = dataStore.data.map { it[USER_AVATAR_KEY] }

    suspend fun saveSession(token: String?, name: String?, email: String?, avatar: String?) {
        dataStore.edit { prefs ->
            if (token != null) prefs[TOKEN_KEY] = token else prefs.remove(TOKEN_KEY)
            if (name != null) prefs[USER_NAME_KEY] = name else prefs.remove(USER_NAME_KEY)
            if (email != null) prefs[USER_EMAIL_KEY] = email else prefs.remove(USER_EMAIL_KEY)
            if (avatar != null) prefs[USER_AVATAR_KEY] = avatar else prefs.remove(USER_AVATAR_KEY)
        }
    }

    suspend fun updateToken(token: String?) {
        dataStore.edit { prefs ->
            if (token != null) prefs[TOKEN_KEY] = token else prefs.remove(TOKEN_KEY)
        }
    }

    suspend fun updateUserName(name: String?) {
        dataStore.edit { prefs ->
            if (name != null) prefs[USER_NAME_KEY] = name else prefs.remove(USER_NAME_KEY)
        }
    }

    suspend fun updateUserEmail(email: String?) {
        dataStore.edit { prefs ->
            if (email != null) prefs[USER_EMAIL_KEY] = email else prefs.remove(USER_EMAIL_KEY)
        }
    }

    suspend fun updateUserAvatar(avatar: String?) {
        dataStore.edit { prefs ->
            if (avatar != null) prefs[USER_AVATAR_KEY] = avatar else prefs.remove(USER_AVATAR_KEY)
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
