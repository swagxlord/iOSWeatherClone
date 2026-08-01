package com.example.iosweatherappclone.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.iosweatherappclone.core.navigation.AppRoute
import com.example.iosweatherappclone.feature.locations.LocationSearchScreen
import com.example.iosweatherappclone.feature.locations.LocationsScreen
import com.example.iosweatherappclone.feature.maps.WeatherMapScreen
import com.example.iosweatherappclone.feature.settings.SettingsScreen
import com.example.iosweatherappclone.feature.weather.WeatherDetailScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navigationState = rememberAppNavigationState()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            if (navigationState.shouldShowWeatherNavigation) {
                WeatherNavigationBar(
                    selectedLocationIndex = navigationState.selectedLocationIndex,
                    locationCount = navigationState.locationCount,
                    onLocationSelected = navigationState::selectLocation,
                    onOpenMap = {
                        navigationState.navigateFromWeather(AppRoute.WeatherMap)
                    },
                    onOpenLocations = {
                        navigationState.navigateFromWeather(AppRoute.Locations)
                    },
                )
            }
        },
    ) { contentPadding ->
        NavDisplay(
            backStack = navigationState.backStack,
            modifier = Modifier.padding(contentPadding),
            onBack = { navigationState.navigateBack() },
            entryProvider = entryProvider {
                entry<AppRoute.WeatherDetail> {
                    WeatherDetailScreen()
                }
                entry<AppRoute.Locations> {
                    LocationsScreen(
                        onSelectLocation = {
                            navigationState.selectLocation(0)
                            navigationState.navigateToWeather()
                        },
                        onSearch = {
                            navigationState.navigateToSecondary(AppRoute.LocationSearch)
                        },
                        onOpenSettings = {
                            navigationState.navigateToSecondary(AppRoute.Settings)
                        },
                    )
                }
                entry<AppRoute.LocationSearch> {
                    LocationSearchScreen(
                        onLocationSelected = {
                            navigationState.selectLocation(0)
                            navigationState.navigateToWeather()
                        },
                        onBack = navigationState::navigateBack,
                    )
                }
                entry<AppRoute.WeatherMap> {
                    WeatherMapScreen()
                }
                entry<AppRoute.Settings> {
                    SettingsScreen(onBack = navigationState::navigateBack)
                }
                entry<AppRoute.WidgetConfiguration> {
                    // Widget setup will supply its feature-owned content in the platform phase.
                    SettingsScreen(onBack = navigationState::navigateBack)
                }
            },
        )
    }
}

@Composable
private fun WeatherNavigationBar(
    selectedLocationIndex: Int,
    locationCount: Int,
    onLocationSelected: (Int) -> Unit,
    onOpenMap: () -> Unit,
    onOpenLocations: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = WeatherNavigationDefaults.Elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(WeatherNavigationDefaults.Height)
                .padding(horizontal = WeatherNavigationDefaults.HorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onOpenMap) {
                Text("Map", style = MaterialTheme.typography.labelMedium)
            }
            LocationCarousel(
                selectedLocationIndex = selectedLocationIndex,
                locationCount = locationCount,
                onLocationSelected = onLocationSelected,
            )
            IconButton(onClick = onOpenLocations) {
                Text("List", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun LocationCarousel(
    selectedLocationIndex: Int,
    locationCount: Int,
    onLocationSelected: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(WeatherNavigationDefaults.DotSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(locationCount) { index ->
            val isSelected = index == selectedLocationIndex
            val isCurrentLocation = index == 0
            val indicatorColor = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.36f)
            }

            if (isCurrentLocation) {
                Box(
                    modifier = Modifier
                        .size(WeatherNavigationDefaults.CurrentLocationSize)
                        .clip(CircleShape)
                        .background(indicatorColor)
                        .clickable { onLocationSelected(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(WeatherNavigationDefaults.CurrentLocationInnerSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .width(
                            if (isSelected) {
                                WeatherNavigationDefaults.SelectedDotWidth
                            } else {
                                WeatherNavigationDefaults.DotSize
                            },
                        )
                        .height(WeatherNavigationDefaults.DotSize)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(indicatorColor)
                        .clickable { onLocationSelected(index) },
                )
            }
        }
    }
}

private object WeatherNavigationDefaults {
    val Height = 72.dp
    val Elevation = 12.dp
    val HorizontalPadding = 24.dp
    val DotSpacing = 8.dp
    val DotSize = 7.dp
    val SelectedDotWidth = 18.dp
    val CurrentLocationSize = 12.dp
    val CurrentLocationInnerSize = 5.dp
}
