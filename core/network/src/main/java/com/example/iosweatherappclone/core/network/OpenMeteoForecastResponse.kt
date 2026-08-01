package com.example.iosweatherappclone.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoForecastResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMilliseconds: Double = 0.0,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    val current: OpenMeteoCurrentWeather? = null,
    val hourly: OpenMeteoHourlyForecast? = null,
    val daily: OpenMeteoDailyForecast? = null,
)

@Serializable
data class OpenMeteoCurrentWeather(
    val time: String,
    val interval: Int,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("relative_humidity_2m") val relativeHumidity: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
    val precipitation: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("cloud_cover") val cloudCover: Int? = null,
    @SerialName("surface_pressure") val surfacePressure: Double? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("wind_direction_10m") val windDirection: Int? = null,
    @SerialName("wind_gusts_10m") val windGusts: Double? = null,
)

@Serializable
data class OpenMeteoHourlyForecast(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature: List<Double> = emptyList(),
    @SerialName("apparent_temperature") val apparentTemperature: List<Double> = emptyList(),
    @SerialName("precipitation_probability") val precipitationProbability: List<Int> = emptyList(),
    val precipitation: List<Double> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
    @SerialName("relative_humidity_2m") val relativeHumidity: List<Int> = emptyList(),
    @SerialName("dew_point_2m") val dewPoint: List<Double> = emptyList(),
    @SerialName("surface_pressure") val surfacePressure: List<Double> = emptyList(),
    val visibility: List<Double> = emptyList(),
    @SerialName("uv_index") val uvIndex: List<Double> = emptyList(),
    @SerialName("wind_speed_10m") val windSpeed: List<Double> = emptyList(),
    @SerialName("wind_direction_10m") val windDirection: List<Int> = emptyList(),
    @SerialName("wind_gusts_10m") val windGusts: List<Double> = emptyList(),
)

@Serializable
data class OpenMeteoDailyForecast(
    val time: List<String>,
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
    @SerialName("temperature_2m_max") val maximumTemperature: List<Double> = emptyList(),
    @SerialName("temperature_2m_min") val minimumTemperature: List<Double> = emptyList(),
    @SerialName("apparent_temperature_max") val maximumApparentTemperature: List<Double> = emptyList(),
    @SerialName("apparent_temperature_min") val minimumApparentTemperature: List<Double> = emptyList(),
    val sunrise: List<String> = emptyList(),
    val sunset: List<String> = emptyList(),
    @SerialName("uv_index_max") val maximumUvIndex: List<Double> = emptyList(),
    @SerialName("precipitation_sum") val precipitationSum: List<Double> = emptyList(),
    @SerialName("precipitation_probability_max") val maximumPrecipitationProbability: List<Int> = emptyList(),
    @SerialName("wind_speed_10m_max") val maximumWindSpeed: List<Double> = emptyList(),
    @SerialName("wind_gusts_10m_max") val maximumWindGusts: List<Double> = emptyList(),
    @SerialName("wind_direction_10m_dominant") val dominantWindDirection: List<Int> = emptyList(),
)
