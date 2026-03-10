package com.example.universalmotorsporttimingcalenda

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.universalmotorsporttimingcalenda.ui.navigation.NavGraph
import com.example.universalmotorsporttimingcalenda.ui.theme.UmtcTheme
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languageCode = prefs.getString("locale", "") ?: ""
        val contextToUse = if (languageCode.isNotEmpty()) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            newBase.createConfigurationContext(config)
        } else {
            newBase
        }
        super.attachBaseContext(contextToUse)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UmtcTheme {
                NavGraph(sessionManager = sessionManager)
            }
        }
    }
}
