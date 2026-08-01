package com.example.iosweatherappclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.iosweatherappclone.navigation.AppNavHost
import com.example.iosweatherappclone.ui.theme.IOSWeatherAppCloneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IOSWeatherAppCloneTheme {
                AppNavHost()
            }
        }
    }
}
