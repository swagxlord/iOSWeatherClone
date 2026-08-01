package com.example.iosweatherappclone.core.network

internal object OpenMeteoApiConstants {
    const val DEFAULT_BASE_URL = "https://api.open-meteo.com"
    const val FORECAST_PATH = "/v1/forecast"
    const val DEFAULT_FORECAST_DAYS = 10
    const val MAX_FORECAST_DAYS = 16
    const val DEFAULT_TIME_ZONE = "auto"

    const val PARAM_LATITUDE = "latitude"
    const val PARAM_LONGITUDE = "longitude"
    const val PARAM_TIME_ZONE = "timezone"
    const val PARAM_FORECAST_DAYS = "forecast_days"
    const val PARAM_TEMPERATURE_UNIT = "temperature_unit"
    const val PARAM_WIND_SPEED_UNIT = "wind_speed_unit"
    const val PARAM_PRECIPITATION_UNIT = "precipitation_unit"
    const val PARAM_CURRENT = "current"
    const val PARAM_HOURLY = "hourly"
    const val PARAM_DAILY = "daily"

    const val TEMPERATURE_UNIT_FAHRENHEIT = "fahrenheit"
    const val WIND_SPEED_UNIT_MPH = "mph"
    const val PRECIPITATION_UNIT_INCH = "inch"
}
