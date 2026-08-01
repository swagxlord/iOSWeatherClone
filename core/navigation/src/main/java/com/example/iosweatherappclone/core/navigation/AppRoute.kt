package com.example.iosweatherappclone.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** Stable, app-owned keys for every addressable destination. */
@Serializable
sealed interface AppRoute : NavKey {
    @Serializable
    data object WeatherDetail : AppRoute

    @Serializable
    data object Locations : AppRoute

    @Serializable
    data object LocationSearch : AppRoute

    @Serializable
    data object WeatherMap : AppRoute

    @Serializable
    data class AlertDetail(val alertId: String) : AppRoute

    @Serializable
    data object Settings : AppRoute

    @Serializable
    data object WidgetConfiguration : AppRoute
}
