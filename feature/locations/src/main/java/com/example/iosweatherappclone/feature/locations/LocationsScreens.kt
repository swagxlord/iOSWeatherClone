package com.example.iosweatherappclone.feature.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocationsScreen(
    onSelectLocation: () -> Unit,
    onSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DestinationLayout("Locations", modifier) {
        Button(onClick = onSelectLocation) { Text("Open current location") }
        Button(onClick = onSearch) { Text("Search locations") }
        OutlinedButton(onClick = onOpenSettings) { Text("Settings") }
    }
}

@Composable
fun LocationSearchScreen(
    onLocationSelected: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DestinationLayout("Location Search", modifier) {
        Text("Search and saved-city results will appear here.")
        Button(onClick = onLocationSelected) { Text("Select sample location") }
        OutlinedButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
private fun DestinationLayout(
    title: String,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        content()
    }
}
