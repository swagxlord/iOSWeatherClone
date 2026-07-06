# PRD: Android Weather App Clone

## 1. Product Summary

Build a fully functioning Android weather app inspired by the iOS Weather app experience. The app should provide a polished, visually rich weather dashboard with current conditions, hourly forecast, 10-day forecast, detailed weather metrics, air quality, precipitation radar, severe alerts, saved locations, widgets, and notifications.

This is a personal/portfolio project, not a commercial public weather product. The app should prioritize clean Android architecture, high-quality Compose UI, realistic weather data, and an iOS-inspired user experience using original branding, original visuals, and non-Apple assets.

## 2. Product Goals

The app should:

1. Show accurate current weather for the user’s current location and saved cities.
2. Provide Apple Weather-style sections:

   * Current conditions header
   * Hourly forecast carousel
   * 10-day forecast
   * Weather detail cards
   * Air quality card
   * Severe alert card
   * Radar/weather map
   * Sunrise/sunset
   * Moon phase
3. Support saved locations with search, delete, reorder, and swipe navigation.
4. Support local settings for units, theme, notifications, and default location.
5. Support Android home screen widgets.
6. Support weather notifications for severe alerts and optional daily summaries.
7. Use clean, testable Android architecture with provider-agnostic weather models.
8. Avoid direct dependency on provider DTOs in the UI layer.

## 3. Non-Goals

This app will not:

1. Use Apple WeatherKit as the primary weather provider.
2. Use Apple-owned icons, animations, copy, layouts, or brand assets.
3. Provide guaranteed enterprise-grade weather accuracy.
4. Support global severe alerts in V1.
5. Support paid subscriptions or user accounts.
6. Require a backend server for the initial implementation.
7. Provide minute-perfect precipitation notifications like Apple Weather or Dark Sky.

## 4. Primary Data Providers

### 4.1 Core Forecast Provider

Use Open-Meteo Forecast API for:

* Current temperature
* Apparent temperature / feels like
* Current condition code
* Day/night state
* Hourly forecast
* Daily forecast
* 10-day forecast
* Precipitation probability
* Rain amount
* Snowfall amount
* Wind speed
* Wind direction
* Wind gusts
* Humidity
* Dew point
* Pressure
* Visibility
* UV index
* Sunrise
* Sunset

### 4.2 Air Quality Provider

Use Open-Meteo Air Quality API for:

* US AQI
* European AQI
* PM2.5
* PM10
* Ozone
* Nitrogen dioxide
* Sulfur dioxide
* Carbon monoxide
* Dust
* Pollen, if available

### 4.3 Location Search Provider

Use Open-Meteo Geocoding API for:

* City search
* Postal code search, if available
* Latitude
* Longitude
* Country
* State/admin area
* Timezone
* Elevation, if useful

### 4.4 Radar Provider

Use RainViewer Weather Maps API for:

* Radar tile frames
* Past radar animation
* Radar timestamp list
* Precipitation map overlay

### 4.5 Severe Weather Alerts Provider

Use National Weather Service API for U.S. alerts:

* Watches
* Warnings
* Advisories
* Alert title/event
* Severity
* Urgency
* Certainty
* Effective time
* Expiration time
* Alert description
* Instructions

For non-U.S. locations, severe alerts may be hidden or shown as unavailable.

## 5. Technical Stack

Use:

* Kotlin
* Jetpack Compose
* Material 3 with custom styling
* Navigation 3 for Compose navigation
* Hilt
* Retrofit or Ktor
* Kotlinx Serialization or Moshi
* Room
* DataStore
* WorkManager
* Jetpack Glance
* MapLibre
* Coil, if image loading is needed
* Kotlin Coroutines
* StateFlow
* JUnit
* Turbine
* MockWebServer
* Compose UI tests

Preferred networking stack:

* Ktor client + Kotlinx Serialization

Acceptable alternative:

* Retrofit + Kotlinx Serialization or Moshi

## 6. App Architecture

Use a modular architecture with MVI-style presentation layers:

```text
:app
:core:model
:core:network
:core:database
:core:datastore
:core:location
:core:navigation
:core:designsystem
:core:testing
:feature:weather
:feature:locations
:feature:maps
:feature:alerts
:feature:settings
:feature:widgets
```

### 6.1 Data Flow

```text
Compose Screen
    ↓
User Intent / UI Event
    ↓
MVI ViewModel / Store
    ↓
Reducer + Effect Handler
    ↓
Use Case
    ↓
Repository Interface
    ↓
Remote Data Sources + Local Cache
    ↓
Provider DTOs
```

Presentation code should follow unidirectional data flow:

* The UI renders immutable screen state.
* User actions are sent as intents/events.
* The MVI layer reduces results into new state.
* One-time actions such as navigation, snackbar messages, and permission prompts are emitted as effects.
* ViewModels may be used as Android lifecycle holders, but their public contract should be MVI-oriented rather than exposing ad hoc mutable methods and state.

### 6.2 Navigation

Use Navigation 3 as the app navigation foundation.

Navigation should be modeled with:

* App-owned route keys for each screen.
* An app-owned back stack that can be saved and restored.
* `NavDisplay` to render destinations from the current back stack.
* Feature-owned destination content, with route definitions kept stable and serializable where needed.
* Navigation effects emitted from MVI stores and handled at the app/navigation host boundary.

Initial route keys should include:

```kotlin
sealed interface AppRoute {
    data object WeatherDetail : AppRoute
    data object Locations : AppRoute
    data object LocationSearch : AppRoute
    data object WeatherMap : AppRoute
    data class AlertDetail(val alertId: String) : AppRoute
    data object Settings : AppRoute
    data object WidgetConfiguration : AppRoute
}
```

### 6.3 Repository Rule

The UI must never consume Open-Meteo, RainViewer, or NWS DTOs directly.

Provider DTOs must be mapped into app-owned domain/UI models.

Example:

```kotlin
interface WeatherRepository {
    suspend fun getWeatherScreen(location: GeoPoint): WeatherScreenData
    suspend fun searchLocations(query: String): List<LocationSearchResult>
    suspend fun getSavedLocations(): Flow<List<SavedLocation>>
    suspend fun saveLocation(location: SavedLocation)
    suspend fun deleteLocation(locationId: String)
    suspend fun getRadarFrames(): List<RadarFrame>
    suspend fun getAlerts(location: GeoPoint): List<WeatherAlert>
}
```

## 7. Main Screens

### 7.1 Weather Detail Screen

The main screen shows weather for one selected location.

Sections:

1. Current weather header
2. Active alert banner, if any
3. Next-hour precipitation summary, if precipitation is expected
4. Hourly forecast carousel
5. 10-day forecast card
6. Weather detail card grid
7. Air quality card
8. Sunrise/sunset card
9. Moon phase card
10. Radar preview card
11. Data attribution/footer

The screen should support:

* Pull to refresh
* Loading state
* Error state
* Stale cache state
* Offline state
* Swipe between saved locations
* Tap cards to open detail screens
* Dynamic background based on weather condition and day/night

### 7.2 Locations Screen

Shows current location and saved locations.

Features:

* Current location card
* Saved city cards
* Search button
* Reorder locations
* Delete locations
* Tap location to open Weather Detail Screen

Each location card should show:

* City name
* Current temperature
* Condition text
* High/low
* Weather icon
* Local time, optional

### 7.3 Location Search Screen

Allows searching for cities.

Features:

* Search input
* Debounced city search
* Result list
* Add/save location
* Empty state
* Error state

Each search result should show:

* City name
* Admin area/state
* Country
* Timezone, optional

### 7.4 Weather Map Screen

Shows radar map.

Features:

* MapLibre base map
* RainViewer radar tile overlay
* Timeline scrubber
* Play/pause radar animation
* Current location marker
* Saved location marker
* Layer selector

V1 map layers:

* Precipitation radar only

Future map layers:

* Temperature
* Wind
* Air quality

### 7.5 Alert Detail Screen

Shows severe weather alert details.

Fields:

* Alert title
* Event type
* Severity
* Urgency
* Certainty
* Source
* Effective time
* Expiration time
* Description
* Instructions

### 7.6 Settings Screen

Settings:

* Temperature unit: Fahrenheit, Celsius, system default
* Wind speed unit: mph, km/h, m/s, knots
* Pressure unit: hPa, inHg
* Precipitation unit: inches, millimeters
* Visibility unit: miles, kilometers
* Theme: system, light, dark
* Default location
* Notifications enabled/disabled
* Severe alert notifications
* Daily summary notifications
* Rain starting/stopping notifications
* Widget location preference

### 7.7 Widget Configuration Screen

Allows the user to pick which location a widget should display.

Widget sizes:

* Small: current temp + condition
* Medium: current temp + hourly preview
* Large: current temp + 5-day forecast

## 8. Core Domain Models

### 8.1 WeatherScreenData

```kotlin
data class WeatherScreenData(
    val location: WeatherLocation,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val details: WeatherDetails,
    val airQuality: AirQuality?,
    val alerts: List<WeatherAlert>,
    val precipitationTimeline: List<PrecipitationTimelinePoint>,
    val astronomy: Astronomy,
    val radarPreview: RadarPreview?,
    val lastUpdated: Instant,
    val isStale: Boolean
)
```

### 8.2 WeatherLocation

```kotlin
data class WeatherLocation(
    val id: String,
    val name: String,
    val adminArea: String?,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val isCurrentLocation: Boolean
)
```

### 8.3 CurrentWeather

```kotlin
data class CurrentWeather(
    val temperature: Double,
    val apparentTemperature: Double?,
    val condition: WeatherCondition,
    val highToday: Double?,
    val lowToday: Double?,
    val isDay: Boolean,
    val humidity: Int?,
    val dewPoint: Double?,
    val pressure: Double?,
    val visibility: Double?,
    val uvIndex: Double?,
    val wind: Wind?,
    val precipitation: Precipitation?,
    val cloudCover: Int?
)
```

### 8.4 HourlyForecast

```kotlin
data class HourlyForecast(
    val time: Instant,
    val temperature: Double,
    val apparentTemperature: Double?,
    val condition: WeatherCondition,
    val precipitationProbability: Int?,
    val precipitationAmount: Double?,
    val rainAmount: Double?,
    val snowAmount: Double?,
    val wind: Wind?,
    val humidity: Int?,
    val pressure: Double?,
    val visibility: Double?,
    val uvIndex: Double?,
    val isDay: Boolean
)
```

### 8.5 DailyForecast

```kotlin
data class DailyForecast(
    val date: LocalDate,
    val condition: WeatherCondition,
    val high: Double,
    val low: Double,
    val apparentHigh: Double?,
    val apparentLow: Double?,
    val precipitationProbabilityMax: Int?,
    val precipitationSum: Double?,
    val rainSum: Double?,
    val snowSum: Double?,
    val sunrise: Instant?,
    val sunset: Instant?,
    val uvIndexMax: Double?,
    val windSpeedMax: Double?,
    val windGustMax: Double?,
    val windDirectionDominantDegrees: Int?
)
```

### 8.6 WeatherDetails

```kotlin
data class WeatherDetails(
    val feelsLike: Double?,
    val humidity: Int?,
    val dewPoint: Double?,
    val pressure: Double?,
    val visibility: Double?,
    val uvIndex: Double?,
    val wind: Wind?,
    val precipitationToday: Precipitation?,
    val cloudCover: Int?
)
```

### 8.7 Wind

```kotlin
data class Wind(
    val speed: Double,
    val directionDegrees: Int?,
    val gust: Double?
)
```

### 8.8 Precipitation

```kotlin
data class Precipitation(
    val probability: Int?,
    val amount: Double?,
    val rainAmount: Double?,
    val snowAmount: Double?,
    val type: PrecipitationType
)

enum class PrecipitationType {
    NONE,
    RAIN,
    SNOW,
    SLEET,
    MIXED,
    UNKNOWN
}
```

### 8.9 PrecipitationTimelinePoint

```kotlin
data class PrecipitationTimelinePoint(
    val time: Instant,
    val probability: Int?,
    val amount: Double?,
    val type: PrecipitationType,
    val intensity: PrecipitationIntensity
)

enum class PrecipitationIntensity {
    NONE,
    LIGHT,
    MODERATE,
    HEAVY
}
```

### 8.10 AirQuality

```kotlin
data class AirQuality(
    val usAqi: Int?,
    val europeanAqi: Int?,
    val category: AirQualityCategory,
    val primaryPollutant: String?,
    val pm25: Double?,
    val pm10: Double?,
    val ozone: Double?,
    val nitrogenDioxide: Double?,
    val sulfurDioxide: Double?,
    val carbonMonoxide: Double?
)

enum class AirQualityCategory {
    GOOD,
    MODERATE,
    UNHEALTHY_FOR_SENSITIVE_GROUPS,
    UNHEALTHY,
    VERY_UNHEALTHY,
    HAZARDOUS,
    UNKNOWN
}
```

### 8.11 Astronomy

```kotlin
data class Astronomy(
    val sunrise: Instant?,
    val sunset: Instant?,
    val daylightDuration: Duration?,
    val moonPhase: MoonPhase?,
    val moonIllumination: Double?,
    val nextFullMoon: LocalDate?
)

enum class MoonPhase {
    NEW_MOON,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS,
    FULL_MOON,
    WANING_GIBBOUS,
    LAST_QUARTER,
    WANING_CRESCENT,
    UNKNOWN
}
```

### 8.12 WeatherAlert

```kotlin
data class WeatherAlert(
    val id: String,
    val title: String,
    val event: String,
    val severity: AlertSeverity,
    val urgency: String?,
    val certainty: String?,
    val description: String,
    val instruction: String?,
    val source: String,
    val effective: Instant?,
    val expires: Instant?
)

enum class AlertSeverity {
    MINOR,
    MODERATE,
    SEVERE,
    EXTREME,
    UNKNOWN
}
```

### 8.13 RadarFrame

```kotlin
data class RadarFrame(
    val time: Instant,
    val tileUrlTemplate: String
)
```

## 9. Weather Condition Mapping

Create an app-owned condition enum.

```kotlin
enum class WeatherCondition {
    CLEAR,
    MOSTLY_CLEAR,
    PARTLY_CLOUDY,
    CLOUDY,
    FOG,
    DRIZZLE,
    RAIN,
    HEAVY_RAIN,
    FREEZING_RAIN,
    SNOW,
    HEAVY_SNOW,
    THUNDERSTORM,
    THUNDERSTORM_WITH_HAIL,
    UNKNOWN
}
```

Map Open-Meteo WMO weather codes into this enum.

The UI should only know about `WeatherCondition`, not raw provider codes.

## 10. API Requirements

### 10.1 Open-Meteo Forecast Request

The app should request:

Current variables:

* temperature_2m
* relative_humidity_2m
* apparent_temperature
* is_day
* precipitation
* rain
* showers
* snowfall
* weather_code
* cloud_cover
* pressure_msl
* surface_pressure
* wind_speed_10m
* wind_direction_10m
* wind_gusts_10m

Hourly variables:

* temperature_2m
* relative_humidity_2m
* dew_point_2m
* apparent_temperature
* precipitation_probability
* precipitation
* rain
* showers
* snowfall
* snow_depth
* weather_code
* pressure_msl
* surface_pressure
* cloud_cover
* visibility
* wind_speed_10m
* wind_direction_10m
* wind_gusts_10m
* uv_index
* is_day

Daily variables:

* weather_code
* temperature_2m_max
* temperature_2m_min
* apparent_temperature_max
* apparent_temperature_min
* sunrise
* sunset
* daylight_duration
* uv_index_max
* precipitation_sum
* rain_sum
* showers_sum
* snowfall_sum
* precipitation_probability_max
* wind_speed_10m_max
* wind_gusts_10m_max
* wind_direction_10m_dominant

Forecast length:

* 10 days minimum
* 16 days acceptable if used internally

Timezone:

* Use location timezone from geocoding result.
* If unavailable, use auto timezone behavior from provider.

### 10.2 Open-Meteo Air Quality Request

Request:

* us_aqi
* european_aqi
* pm10
* pm2_5
* carbon_monoxide
* nitrogen_dioxide
* sulphur_dioxide
* ozone
* dust
* uv_index
* alder_pollen, birch_pollen, grass_pollen, mugwort_pollen, olive_pollen, ragweed_pollen if desired

### 10.3 NWS Alerts Request

For U.S. coordinates:

* Fetch active alerts by point if supported.
* Otherwise resolve location to grid/zone and fetch alerts by zone.
* Include a valid User-Agent header.

If the selected location is outside the U.S.:

* Do not call NWS.
* Show no severe-alert card unless another provider is added later.

### 10.4 RainViewer Request

Fetch weather map metadata:

```text
https://api.rainviewer.com/public/weather-maps.json
```

Use returned radar frames to build map tile URL templates.

The map screen should support:

* Most recent frame
* Past frames
* Timeline animation
* Refreshing frames

## 11. Local Storage

### 11.1 Room Tables

Create Room entities for:

```text
saved_locations
cached_weather
cached_alerts
radar_frames
```

### 11.2 DataStore Preferences

Store:

```text
temperature_unit
wind_unit
pressure_unit
precipitation_unit
visibility_unit
theme_mode
selected_location_id
notifications_enabled
severe_alert_notifications_enabled
daily_summary_notifications_enabled
rain_notifications_enabled
widget_location_id
```

## 12. Caching Rules

### 12.1 Forecast Cache

* Cache weather screen data by location ID.
* Consider data fresh for 15 minutes.
* Show stale data if cache is older than 15 minutes and network fails.
* Show last updated timestamp.
* Pull-to-refresh should bypass freshness window.

### 12.2 Air Quality Cache

* Cache by location ID.
* Consider fresh for 30 minutes.

### 12.3 Radar Cache

* Cache radar frame metadata.
* Refresh when opening the map screen if older than 10 minutes.

### 12.4 Alerts Cache

* Cache by location ID.
* Consider fresh for 15 minutes.
* Dedupe alerts by alert ID.

## 13. Notifications

Use WorkManager for periodic weather checks.

Important limitation:

* Periodic WorkManager tasks have a 15-minute minimum interval.
* Rain starting/stopping notifications should be best-effort, not minute-perfect.

### 13.1 Notification Types

Implement:

1. Severe weather alert notification
2. Daily weather summary notification
3. Rain starting soon notification
4. Rain stopping soon notification

### 13.2 V1 Notification Scope

V1 should implement:

* Severe weather alerts
* Daily weather summary

V2 should implement:

* Rain starting soon
* Rain stopping soon

### 13.3 Notification Deduping

The app must not repeatedly notify the same alert.

Store:

```text
notified_alert_ids
last_daily_summary_date
last_rain_notification_state
```

## 14. Widgets

Use Jetpack Glance.

### 14.1 Widget Types

Small widget:

* Location
* Current temperature
* Condition icon
* Condition text

Medium widget:

* Location
* Current temperature
* Condition
* High/low
* 4-hour preview

Large widget:

* Location
* Current temperature
* Condition
* High/low
* 5-day forecast

### 14.2 Widget Data

Widgets should use cached data first.

If cache is stale:

* Trigger background refresh where possible.
* Show stale state gracefully.
* Never block widget rendering on a network request.

## 15. UI/UX Requirements

### 15.1 Visual Style

The app should feel inspired by Apple Weather, but must use original styling.

Use:

* Large typography
* Weather-based gradients
* Rounded glass-like cards
* Subtle shadows
* Smooth transitions
* Animated condition backgrounds if feasible
* Original icons or open-source icons
* Material 3 components customized for a softer weather-app feel

Do not use:

* Apple SF Symbols
* Apple Weather icons
* Apple-owned gradients/assets
* Apple branding
* Exact Apple copy

### 15.2 Dynamic Backgrounds

Background should be based on:

* Weather condition
* Day/night
* Rain/snow/thunder state
* Optional time of day

Examples:

```text
CLEAR + day = blue sunny gradient
CLEAR + night = dark navy gradient
RAIN = gray-blue rainy gradient
SNOW = pale blue/white gradient
THUNDERSTORM = dark purple-gray gradient
FOG = muted gray gradient
```

### 15.3 Cards

Cards should be reusable:

```kotlin
@Composable
fun WeatherCard(
    title: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
)
```

Detail cards:

* UV Index
* Wind
* Humidity
* Feels Like
* Visibility
* Pressure
* Precipitation
* Sunrise/Sunset
* Air Quality
* Moon Phase

## 16. State Management

Use MVI for screen state management. Each screen should define:

* A sealed or data-class UI state.
* A sealed intent/event type for user and lifecycle actions.
* A sealed effect type for one-shot actions.
* A reducer path that makes state transitions explicit.

```kotlin
sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val data: WeatherScreenData) : WeatherUiState
    data class Error(
        val message: String,
        val cachedData: WeatherScreenData? = null
    ) : WeatherUiState
}
```

Each MVI ViewModel/store should expose immutable state and an intent entry point:

```kotlin
val uiState: StateFlow<WeatherUiState>

fun onIntent(intent: WeatherIntent)
```

Use one-shot effects for:

* Snackbar messages
* Navigation
* Permission prompts
* Refresh failures

## 17. Permissions

The app needs:

### Location

Required for current location weather.

Handle:

* Permission not requested
* Permission granted
* Permission denied
* Permission permanently denied
* Location disabled

### Notifications

Required on Android 13+.

Handle:

* Permission not requested
* Permission granted
* Permission denied

## 18. Error States

The app should handle:

* No internet
* API failure
* Invalid API response
* Location permission denied
* Location unavailable
* Empty geocoding results
* No saved locations
* Radar unavailable
* Alerts unavailable
* Air quality unavailable

Error UI should prefer graceful fallback:

* Show cached data if available.
* Show partial sections if some APIs fail.
* Do not fail the entire weather screen because air quality, radar, or alerts failed.

## 19. Build Phases

### Phase 1: Core Forecast App

Implement:

* Project/module setup
* Open-Meteo Forecast API
* WeatherRepository
* Current weather header
* Hourly forecast carousel
* 10-day forecast card
* Basic detail cards
* Location search
* Saved locations
* Room/DataStore
* Pull to refresh
* Loading/error states

Acceptance criteria:

* User can search for a city.
* User can save a city.
* User can open a saved city.
* User can see current, hourly, and 10-day weather.
* User can refresh weather.
* Data is cached locally.

### Phase 2: Visual Polish

Implement:

* Weather-based backgrounds
* Condition icon mapping
* Temperature range bars
* Swipe between saved locations
* Better empty/error states
* Unit settings
* Smooth card animations

Acceptance criteria:

* App visually resembles a premium weather app.
* User can change temperature/wind/pressure/precipitation units.
* Forecast bars scale correctly across the 10-day forecast.

### Phase 3: Expanded Weather Data

Implement:

* Air quality API
* AQI card
* AQI detail screen
* Moon phase calculation
* Better precipitation timeline
* NWS severe alerts
* Alert detail screen

Acceptance criteria:

* AQI appears when available.
* Severe alerts appear for U.S. locations.
* Alert details can be opened.
* Moon phase card displays calculated moon phase.
* Precipitation timeline shows next-hour-style summary.

### Phase 4: Weather Maps

Implement:

* MapLibre map screen
* RainViewer radar metadata fetch
* Radar tile overlay
* Timeline scrubber
* Play/pause animation
* Location marker

Acceptance criteria:

* User can open radar map.
* Radar overlay appears over base map.
* User can animate radar frames.
* Radar unavailable state is handled.

### Phase 5: Android Platform Features

Implement:

* Severe alert notifications
* Daily weather summary notification
* Small widget
* Medium widget
* Large widget
* Widget configuration screen

Acceptance criteria:

* User can enable notifications.
* User receives severe alert notification when a new NWS alert is active.
* User can place a widget on the home screen.
* Widget displays cached weather.
* Widget updates after weather refresh.

### Phase 6: Optional Advanced Features

Implement if desired:

* Rain starting/stopping notifications
* Historical averages
* More map layers
* Multiple themes
* Animated weather backgrounds
* Tablet layout
* Wear OS companion, optional

## 20. Testing Requirements

### 20.1 Unit Tests

Test:

* Open-Meteo DTO mapping
* Weather condition mapping
* Unit conversion
* AQI category mapping
* Alert severity mapping
* Precipitation intensity mapping
* Cache freshness logic
* Notification dedupe logic
* Moon phase calculation

### 20.2 Repository Tests

Test:

* Successful weather fetch
* Partial failure with forecast success but AQI failure
* Network failure with cache fallback
* Network failure without cache
* Saved location CRUD
* Alert fetch for U.S. location
* Alert skip for non-U.S. location

### 20.3 UI Tests

Test:

* Weather screen loading state
* Weather screen success state
* Weather screen error state
* Search city flow
* Save city flow
* Settings unit change
* Alert detail navigation
* Map screen unavailable state

## 21. Acceptance Criteria for Full App

The app is considered fully functioning when:

1. User can grant location permission and see current location weather.
2. User can search and save multiple locations.
3. User can view current, hourly, and 10-day forecasts.
4. User can view weather detail cards.
5. User can view AQI when available.
6. User can view severe weather alerts for U.S. locations.
7. User can view a radar map.
8. User can change units.
9. User can use the app offline with cached data.
10. User can receive severe alert notifications.
11. User can add a home screen weather widget.
12. The app handles API errors gracefully.
13. The UI does not depend directly on API DTOs.
14. The app uses original assets and does not copy Apple-owned resources.

## 22. Implementation Guidance for Codex

When implementing this app:

1. Start with the domain models before UI.
2. Create provider DTOs separately from domain models.
3. Build mappers from provider DTOs to domain models.
4. Implement repositories behind interfaces.
5. Build the Weather Detail Screen with fake data first.
6. Replace fake data with Open-Meteo integration.
7. Add cache after the first successful network integration.
8. Add location search and saved locations.
9. Add detail cards.
10. Add AQI, alerts, radar, widgets, and notifications in later phases.

Do not implement all features in one pass. Build vertically by phase.

## 23. Initial Codex Task

First implementation task:

Create the Android project structure, core domain models, fake weather repository, and first version of the Weather Detail Screen using Jetpack Compose.

The first screen should render:

* City name
* Current temperature
* Condition
* High/low
* Hourly forecast carousel
* 10-day forecast card
* Weather detail card grid

Use fake hardcoded weather data for the first UI pass. Do not integrate APIs until the UI and domain model shape are stable.
