package com.example.iosweatherappclone.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavBackStack
import com.example.iosweatherappclone.core.navigation.AppRoute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppNavigationStateTest {
    @Test
    fun currentRoute_returnsLastAppRoute() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Settings,
        )

        assertEquals(AppRoute.Settings, state.currentRoute)
    }

    @Test
    fun currentRoute_returnsNullWhenBackStackIsEmpty() {
        val state = appNavigationState()

        assertNull(state.currentRoute)
    }

    @Test
    fun shouldShowWeatherNavigation_isTrueForWeatherDetail() {
        val state = appNavigationState(AppRoute.WeatherDetail)

        assertTrue(state.shouldShowWeatherNavigation)
    }

    @Test
    fun shouldShowWeatherNavigation_isFalseForWeatherMap() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.WeatherMap,
        )

        assertFalse(state.shouldShowWeatherNavigation)
    }

    @Test
    fun shouldShowWeatherNavigation_isFalseForLocations() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Locations,
        )

        assertFalse(state.shouldShowWeatherNavigation)
    }

    @Test
    fun shouldShowWeatherNavigation_isFalseForSecondaryRoutes() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Settings,
        )

        assertFalse(state.shouldShowWeatherNavigation)
    }

    @Test
    fun navigateToWeather_replacesBackStackWithWeatherDetail() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Locations,
            AppRoute.LocationSearch,
        )

        state.navigateToWeather()

        assertEquals(listOf(AppRoute.WeatherDetail), state.backStack.toList())
    }

    @Test
    fun navigateFromWeather_replacesBackStackWithWeatherAndDestination() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Settings,
        )

        state.navigateFromWeather(AppRoute.Locations)

        assertEquals(
            listOf(AppRoute.WeatherDetail, AppRoute.Locations),
            state.backStack.toList(),
        )
    }

    @Test
    fun navigateToSecondary_pushesRouteOntoBackStack() {
        val state = appNavigationState(AppRoute.Locations)

        state.navigateToSecondary(AppRoute.LocationSearch)

        assertEquals(
            listOf(AppRoute.Locations, AppRoute.LocationSearch),
            state.backStack.toList(),
        )
    }

    @Test
    fun navigateBack_popsLastRouteWhenMoreThanOneRouteExists() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            AppRoute.Settings,
        )

        state.navigateBack()

        assertEquals(listOf(AppRoute.WeatherDetail), state.backStack.toList())
    }

    @Test
    fun navigateBack_doesNotPopOnlyRoute() {
        val state = appNavigationState(AppRoute.WeatherDetail)

        state.navigateBack()

        assertEquals(listOf(AppRoute.WeatherDetail), state.backStack.toList())
    }

    @Test
    fun selectLocation_updatesSelectedLocationIndex() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            locationCount = 4,
        )

        state.selectLocation(2)

        assertEquals(2, state.selectedLocationIndex)
    }

    @Test
    fun selectLocation_clampsIndexToLocationRange() {
        val state = appNavigationState(
            AppRoute.WeatherDetail,
            locationCount = 4,
        )

        state.selectLocation(99)

        assertEquals(3, state.selectedLocationIndex)
    }

    @Test
    fun defaultLocationCount_isCurrentLocationOnly() {
        val state = appNavigationState(AppRoute.WeatherDetail)

        assertEquals(1, state.locationCount)
    }

    @Test
    fun selectLocation_clampsToCurrentLocationWhenOnlyCurrentLocationExists() {
        val state = appNavigationState(AppRoute.WeatherDetail)

        state.selectLocation(99)

        assertEquals(0, state.selectedLocationIndex)
    }

    private fun appNavigationState(
        vararg routes: AppRoute,
        locationCount: Int = 1,
    ): AppNavigationState {
        val backStack: NavBackStack = mutableStateListOf(*routes)
        return AppNavigationState(
            backStack = backStack,
            locationCount = locationCount,
        )
    }
}
