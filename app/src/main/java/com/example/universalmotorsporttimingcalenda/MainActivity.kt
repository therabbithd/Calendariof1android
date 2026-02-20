package com.example.universalmotorsporttimingcalenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.universalmotorsporttimingcalenda.ui.navigation.NavGraph
import com.example.universalmotorsporttimingcalenda.ui.theme.UmtcTheme
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var sessionManager: SessionManager

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
