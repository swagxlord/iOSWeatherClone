package com.example.iosweatherappclone.core.network

enum class OpenMeteoCurrentField(val apiName: String) {
    Temperature("temperature_2m"),
    ApparentTemperature("apparent_temperature"),
    RelativeHumidity("relative_humidity_2m"),
    IsDay("is_day"),
    Precipitation("precipitation"),
    WeatherCode("weather_code"),
    CloudCover("cloud_cover"),
    SurfacePressure("surface_pressure"),
    WindSpeed("wind_speed_10m"),
    WindDirection("wind_direction_10m"),
    WindGusts("wind_gusts_10m"),
}

enum class OpenMeteoHourlyField(val apiName: String) {
    Temperature("temperature_2m"),
    ApparentTemperature("apparent_temperature"),
    PrecipitationProbability("precipitation_probability"),
    Precipitation("precipitation"),
    WeatherCode("weather_code"),
    RelativeHumidity("relative_humidity_2m"),
    DewPoint("dew_point_2m"),
    SurfacePressure("surface_pressure"),
    Visibility("visibility"),
    UvIndex("uv_index"),
    WindSpeed("wind_speed_10m"),
    WindDirection("wind_direction_10m"),
    WindGusts("wind_gusts_10m"),
}

enum class OpenMeteoDailyField(val apiName: String) {
    WeatherCode("weather_code"),
    MaximumTemperature("temperature_2m_max"),
    MinimumTemperature("temperature_2m_min"),
    MaximumApparentTemperature("apparent_temperature_max"),
    MinimumApparentTemperature("apparent_temperature_min"),
    Sunrise("sunrise"),
    Sunset("sunset"),
    MaximumUvIndex("uv_index_max"),
    PrecipitationSum("precipitation_sum"),
    MaximumPrecipitationProbability("precipitation_probability_max"),
    MaximumWindSpeed("wind_speed_10m_max"),
    MaximumWindGusts("wind_gusts_10m_max"),
    DominantWindDirection("wind_direction_10m_dominant"),
}
