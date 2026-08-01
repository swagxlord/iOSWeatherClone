package com.example.iosweatherappclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.iosweatherappclone.core.navigation.AppRoute

@Composable
fun rememberAppNavigationState(): AppNavigationState {
    val backStack = rememberNavBackStack(AppRoute.WeatherDetail)
    return remember(backStack) {
        AppNavigationState(backStack)
    }
}

class AppNavigationState(
    val backStack: NavBackStack,
    val locationCount: Int = 1,
) {
    var selectedLocationIndex by mutableIntStateOf(0)
        private set

    val currentRoute: AppRoute?
        get() = backStack.lastOrNull() as? AppRoute

    val shouldShowWeatherNavigation: Boolean
        get() = currentRoute == AppRoute.WeatherDetail

    fun navigateToWeather() {
        backStack.clear()
        backStack.add(AppRoute.WeatherDetail)
    }

    fun navigateFromWeather(route: AppRoute) {
        backStack.clear()
        backStack.add(AppRoute.WeatherDetail)
        backStack.add(route)
    }

    fun navigateToSecondary(route: AppRoute) {
        backStack.add(route)
    }

    fun selectLocation(index: Int) {
        selectedLocationIndex = index.coerceIn(0, (locationCount - 1).coerceAtLeast(0))
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}
