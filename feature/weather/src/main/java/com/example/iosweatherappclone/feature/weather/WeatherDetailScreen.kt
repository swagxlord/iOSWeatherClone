package com.example.iosweatherappclone.feature.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherDetailScreen(
    onOpenLocations: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenAlert: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Weather Detail", style = MaterialTheme.typography.headlineLarge)
        Text("Your forecast will appear here.", modifier = Modifier.padding(top = 8.dp))
        Row(
            modifier = Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = onOpenLocations) { Text("Locations") }
            Button(onClick = onOpenMap) { Text("Map") }
        }
        Button(
            onClick = { onOpenAlert("preview-alert") },
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text("Preview alert")
        }
    }
}
