package com.example.iosweatherappclone.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import com.example.iosweatherappclone.core.network.OpenMeteoApiConstants.DEFAULT_BASE_URL
import com.example.iosweatherappclone.core.network.OpenMeteoApiConstants.DEFAULT_FORECAST_DAYS

class OpenMeteoWeatherApi(
    private val httpClient: HttpClient,
    private val baseUrl: String = DEFAULT_BASE_URL,
) {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        timeZone: String = OpenMeteoApiConstants.DEFAULT_TIME_ZONE,
        forecastDays: Int = DEFAULT_FORECAST_DAYS,
    ): OpenMeteoForecastResponse {
        val request = OpenMeteoForecastRequest.Builder(latitude, longitude)
            .timeZone(timeZone)
            .forecastDays(forecastDays)
            .build()

        return getForecast(request)
    }

    suspend fun getForecast(request: OpenMeteoForecastRequest): OpenMeteoForecastResponse {
        return httpClient.get("${baseUrl.trimEnd('/')}${OpenMeteoApiConstants.FORECAST_PATH}") {
            request.parameters.forEach { (name, value) -> parameter(name, value) }
        }.body()
    }
}
