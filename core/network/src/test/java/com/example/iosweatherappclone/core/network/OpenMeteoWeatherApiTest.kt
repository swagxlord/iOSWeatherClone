package com.example.iosweatherappclone.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenMeteoWeatherApiTest {
    @Test
    fun getForecast_buildsRequestAndParsesResponse() = runBlocking {
        var requestedUrl = ""
        val engine = MockEngine { request ->
            requestedUrl = request.url.toString()
            respond(
                content = FORECAST_JSON,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = testClient(engine)

        val result = OpenMeteoWeatherApi(client, "https://weather.test").getForecast(
            latitude = 40.7128,
            longitude = -74.006,
            timeZone = "America/New_York",
        )

        assertTrue(requestedUrl.startsWith("https://weather.test/v1/forecast?"))
        assertTrue(requestedUrl.contains("latitude=40.7128"))
        assertTrue(requestedUrl.contains("longitude=-74.006"))
        assertTrue(requestedUrl.contains("timezone=America%2FNew_York"))
        assertTrue(requestedUrl.contains("forecast_days=10"))
        assertTrue(requestedUrl.contains("current="))
        assertTrue(requestedUrl.contains("hourly="))
        assertTrue(requestedUrl.contains("daily="))
        assertEquals(72.5, requireNotNull(result.current).temperature)
        assertEquals(61, result.current.relativeHumidity)
        assertEquals(listOf(72.5, 71.0), requireNotNull(result.hourly).temperature)
        assertEquals(listOf(78.0), requireNotNull(result.daily).maximumTemperature)
        assertEquals("America/New_York", result.timezone)
        client.close()
    }

    @Test
    fun getForecast_acceptsCustomFieldSelection() = runBlocking {
        var requestedUrl = ""
        val client = testClient(MockEngine { request ->
            requestedUrl = request.url.toString()
            respond(
                content = FORECAST_JSON,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        })
        val request = OpenMeteoForecastRequest.Builder(40.7128, -74.006)
            .currentFields(OpenMeteoCurrentField.Temperature, OpenMeteoCurrentField.WeatherCode)
            .hourlyFields(OpenMeteoHourlyField.Temperature)
            .dailyFields(OpenMeteoDailyField.MaximumTemperature, OpenMeteoDailyField.MinimumTemperature)
            .build()

        OpenMeteoWeatherApi(client, "https://weather.test").getForecast(request)

        assertTrue(requestedUrl.contains("current=temperature_2m%2Cweather_code"))
        assertTrue(requestedUrl.contains("hourly=temperature_2m"))
        assertTrue(requestedUrl.contains("daily=temperature_2m_max%2Ctemperature_2m_min"))
        assertTrue(!requestedUrl.contains("apparent_temperature"))
        client.close()
    }

    @Test(expected = IllegalArgumentException::class)
    fun getForecast_rejectsInvalidLatitude() {
        runBlocking {
            OpenMeteoWeatherApi(testClient(MockEngine { error("Request should not run") }))
                .getForecast(latitude = 91.0, longitude = 0.0)
        }
    }

    private fun testClient(engine: MockEngine) = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private companion object {
        val FORECAST_JSON = """
            {
              "latitude": 40.71, "longitude": -74.01,
              "generationtime_ms": 0.12, "utc_offset_seconds": -14400,
              "timezone": "America/New_York", "timezone_abbreviation": "GMT-4", "elevation": 32.0,
              "current": {
                "time": "2026-08-01T12:00", "interval": 900,
                "temperature_2m": 72.5, "apparent_temperature": 73.1,
                "relative_humidity_2m": 61, "is_day": 1, "precipitation": 0.0,
                "weather_code": 1, "cloud_cover": 20, "surface_pressure": 1013.2,
                "wind_speed_10m": 8.3, "wind_direction_10m": 220, "wind_gusts_10m": 13.0
              },
              "hourly": {
                "time": ["2026-08-01T12:00", "2026-08-01T13:00"],
                "temperature_2m": [72.5, 71.0], "apparent_temperature": [73.1, 71.5],
                "precipitation_probability": [10, 15], "precipitation": [0.0, 0.0],
                "weather_code": [1, 2], "relative_humidity_2m": [61, 64],
                "dew_point_2m": [58.5, 58.8], "surface_pressure": [1013.2, 1013.0],
                "visibility": [52800.0, 50000.0], "uv_index": [5.2, 4.8],
                "wind_speed_10m": [8.3, 8.8], "wind_direction_10m": [220, 225],
                "wind_gusts_10m": [13.0, 14.0]
              },
              "daily": {
                "time": ["2026-08-01"], "weather_code": [2],
                "temperature_2m_max": [78.0], "temperature_2m_min": [64.0],
                "apparent_temperature_max": [80.0], "apparent_temperature_min": [64.0],
                "sunrise": ["2026-08-01T05:52"], "sunset": ["2026-08-01T20:12"],
                "uv_index_max": [6.1], "precipitation_sum": [0.02],
                "precipitation_probability_max": [25], "wind_speed_10m_max": [11.0],
                "wind_gusts_10m_max": [19.0], "wind_direction_10m_dominant": [215]
              },
              "ignored_provider_field": "safe to ignore"
            }
        """.trimIndent()
    }
}
