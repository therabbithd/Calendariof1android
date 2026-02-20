package com.example.universalmotorsporttimingcalenda.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    var token: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var userAvatar: String? = null
}
