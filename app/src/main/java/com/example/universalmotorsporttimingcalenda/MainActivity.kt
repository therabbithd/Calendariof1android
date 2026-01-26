package com.example.universalmotorsporttimingcalenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.universalmotorsporttimingcalenda.ui.navigation.NavGraph
import com.example.universalmotorsporttimingcalenda.ui.theme.UmtcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UmtcTheme {
                NavGraph()
            }
        }
    }
}
