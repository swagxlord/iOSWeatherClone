package com.example.iosweatherappclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.iosweatherappclone.core.navigation.AppRoute
import com.example.iosweatherappclone.feature.alerts.AlertDetailScreen
import com.example.iosweatherappclone.feature.locations.LocationSearchScreen
import com.example.iosweatherappclone.feature.locations.LocationsScreen
import com.example.iosweatherappclone.feature.maps.WeatherMapScreen
import com.example.iosweatherappclone.feature.settings.SettingsScreen
import com.example.iosweatherappclone.feature.weather.WeatherDetailScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppRoute.WeatherDetail)

    fun navigateToPrimary(route: AppRoute) {
        backStack.clear()
        backStack.add(route)
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
        entryProvider = entryProvider {
            entry<AppRoute.WeatherDetail> {
                WeatherDetailScreen(
                    onOpenLocations = { navigateToPrimary(AppRoute.Locations) },
                    onOpenMap = { navigateToPrimary(AppRoute.WeatherMap) },
                    onOpenAlert = { backStack.add(AppRoute.AlertDetail(it)) },
                )
            }
            entry<AppRoute.Locations> {
                LocationsScreen(
                    onSelectLocation = { navigateToPrimary(AppRoute.WeatherDetail) },
                    onSearch = { backStack.add(AppRoute.LocationSearch) },
                    onOpenSettings = { backStack.add(AppRoute.Settings) },
                )
            }
            entry<AppRoute.LocationSearch> {
                LocationSearchScreen(
                    onLocationSelected = { navigateToPrimary(AppRoute.WeatherDetail) },
                )
            }
            entry<AppRoute.WeatherMap> {
                WeatherMapScreen(
                    onOpenWeather = { navigateToPrimary(AppRoute.WeatherDetail) },
                )
            }
            entry<AppRoute.AlertDetail> { route ->
                AlertDetailScreen(alertId = route.alertId)
            }
            entry<AppRoute.Settings> { SettingsScreen() }
            entry<AppRoute.WidgetConfiguration> {
                // Widget setup will supply its feature-owned content in the platform phase.
                SettingsScreen()
            }
        },
    )
}
