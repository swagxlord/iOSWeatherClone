package com.example.iosweatherappclone.core.network

class OpenMeteoForecastRequest private constructor(
    internal val parameters: Map<String, String>,
) {
    class Builder(
        private val latitude: Double,
        private val longitude: Double,
    ) {
        private var timeZone: String = OpenMeteoApiConstants.DEFAULT_TIME_ZONE
        private var forecastDays: Int = OpenMeteoApiConstants.DEFAULT_FORECAST_DAYS
        private var currentFields = OpenMeteoCurrentField.entries.toList()
        private var hourlyFields = OpenMeteoHourlyField.entries.toList()
        private var dailyFields = OpenMeteoDailyField.entries.toList()

        fun timeZone(value: String) = apply {
            timeZone = value
        }

        fun forecastDays(value: Int) = apply {
            forecastDays = value
        }

        fun currentFields(vararg fields: OpenMeteoCurrentField) = apply {
            currentFields = fields.distinct()
        }

        fun hourlyFields(vararg fields: OpenMeteoHourlyField) = apply {
            hourlyFields = fields.distinct()
        }

        fun dailyFields(vararg fields: OpenMeteoDailyField) = apply {
            dailyFields = fields.distinct()
        }

        fun build(): OpenMeteoForecastRequest {
            require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90." }
            require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180." }
            require(timeZone.isNotBlank()) { "Time zone must not be blank." }
            require(forecastDays in 1..OpenMeteoApiConstants.MAX_FORECAST_DAYS) {
                "Forecast days must be between 1 and ${OpenMeteoApiConstants.MAX_FORECAST_DAYS}."
            }

            val parameters = linkedMapOf(
                OpenMeteoApiConstants.PARAM_LATITUDE to latitude.toString(),
                OpenMeteoApiConstants.PARAM_LONGITUDE to longitude.toString(),
                OpenMeteoApiConstants.PARAM_TIME_ZONE to timeZone,
                OpenMeteoApiConstants.PARAM_FORECAST_DAYS to forecastDays.toString(),
                OpenMeteoApiConstants.PARAM_TEMPERATURE_UNIT to OpenMeteoApiConstants.TEMPERATURE_UNIT_FAHRENHEIT,
                OpenMeteoApiConstants.PARAM_WIND_SPEED_UNIT to OpenMeteoApiConstants.WIND_SPEED_UNIT_MPH,
                OpenMeteoApiConstants.PARAM_PRECIPITATION_UNIT to OpenMeteoApiConstants.PRECIPITATION_UNIT_INCH,
            )
            parameters.addFields(OpenMeteoApiConstants.PARAM_CURRENT, currentFields) { it.apiName }
            parameters.addFields(OpenMeteoApiConstants.PARAM_HOURLY, hourlyFields) { it.apiName }
            parameters.addFields(OpenMeteoApiConstants.PARAM_DAILY, dailyFields) { it.apiName }
            return OpenMeteoForecastRequest(parameters)
        }

        private fun <T> MutableMap<String, String>.addFields(
            parameterName: String,
            fields: List<T>,
            apiName: (T) -> String,
        ) {
            if (fields.isNotEmpty()) put(parameterName, fields.joinToString(",", transform = apiName))
        }
    }
}
