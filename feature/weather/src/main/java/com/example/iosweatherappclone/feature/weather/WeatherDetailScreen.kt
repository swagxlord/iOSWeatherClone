package com.example.iosweatherappclone.feature.weather

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val NightTop = Color(0xFF263447)
private val NightBottom = Color(0xFF101827)
private val CardColor = Color(0xB82B3546)
private val PrimaryText = Color(0xFFF7F8FC)
private val SecondaryText = Color(0xFF9CA8BD)
private val Divider = Color(0x2EFFFFFF)
private val RainBlue = Color(0xFF48D5FF)

enum class WeatherCondition { Clear, Cloudy, Rain, Thunderstorm }

@Immutable
data class HourForecast(
    val time: String,
    val temperature: Int,
    val condition: WeatherCondition,
    val precipitationChance: Int? = null,
)

@Immutable
data class DayForecast(
    val day: String,
    val low: Int,
    val high: Int,
    val condition: WeatherCondition,
    val precipitationChance: Int? = null,
)

@Immutable
data class WeatherDetailUiState(
    val location: String,
    val temperature: Int,
    val conditionLabel: String,
    val condition: WeatherCondition,
    val high: Int,
    val low: Int,
    val isNight: Boolean,
    val summary: String,
    val hourly: List<HourForecast>,
    val daily: List<DayForecast>,
)

@Composable
fun WeatherDetailScreen(
    modifier: Modifier = Modifier,
    state: WeatherDetailUiState = remember { sampleCloudyNightWeather() },
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedWeatherBackground(
            condition = state.condition,
            isNight = state.isNight,
            modifier = Modifier.fillMaxSize(),
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 28.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                CurrentConditionsHeader(
                    state = state,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(top = 54.dp, bottom = 54.dp),
                )
            }
            item { HourlyForecastCard(state.summary, state.hourly) }
            item { TenDayForecastCard(state.daily) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(
                        icon = "♨",
                        title = "FEELS LIKE",
                        value = "76°",
                        description = "It feels warmer than the actual temperature.",
                        modifier = Modifier.weight(1f),
                    )
                    UvIndexCard(modifier = Modifier.weight(1f))
                }
            }
            item { WindCard() }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SunriseCard(modifier = Modifier.weight(1f))
                    MetricCard(
                        icon = "●",
                        title = "PRECIPITATION",
                        value = "1.25\"",
                        description = "in last 24h\n\n.2\" expected in next 24h.",
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("◉", "VISIBILITY", "9 mi", "Clear view.", Modifier.weight(1f))
                    MetricCard("≈", "HUMIDITY", "95%", "The dew point is 73° right now.", Modifier.weight(1f))
                }
            }
            item { AirQualityCard() }
            item { WeatherAlertCard() }
            item { FooterCard() }
            item { Spacer(Modifier.navigationBarsPadding()) }
        }
    }
}

@Composable
private fun CurrentConditionsHeader(state: WeatherDetailUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("⌖  HOME", color = PrimaryText, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(state.location, color = PrimaryText, fontSize = 40.sp, fontWeight = FontWeight.Light)
        Text("${state.temperature}°", color = PrimaryText, fontSize = 86.sp, fontWeight = FontWeight.Thin, lineHeight = 90.sp)
        Text(state.conditionLabel, color = SecondaryText, fontSize = 25.sp)
        Text("H:${state.high}°  L:${state.low}°", color = PrimaryText, fontSize = 22.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AnimatedWeatherBackground(
    condition: WeatherCondition,
    isNight: Boolean,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "weather background")
    val drift by transition.animateFloat(
        initialValue = -0.12f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(tween(18_000), RepeatMode.Reverse),
        label = "cloud drift",
    )
    val breathe by transition.animateFloat(
        initialValue = 0.72f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6_500), RepeatMode.Reverse),
        label = "cloud opacity",
    )
    Canvas(modifier = modifier) {
        drawRect(
            Brush.verticalGradient(
                if (isNight) listOf(NightTop, NightBottom) else listOf(Color(0xFF4B91C4), Color(0xFF95C7E5)),
            ),
        )
        if (condition != WeatherCondition.Clear) {
            val clouds = listOf(
                Triple(0.12f, 0.10f, 0.38f), Triple(0.69f, 0.15f, 0.45f),
                Triple(0.38f, 0.30f, 0.50f), Triple(0.84f, 0.42f, 0.40f),
                Triple(0.05f, 0.54f, 0.43f), Triple(0.56f, 0.66f, 0.56f),
            )
            clouds.forEachIndexed { index, (x, y, scale) ->
                val shiftedX = (x + drift * if (index % 2 == 0) 1f else -0.65f) * size.width
                val radius = size.width * scale * 0.24f
                val cloudColor = if (isNight) Color(0xFF7D8999) else Color.White
                repeat(5) { lobe ->
                    drawCircle(
                        color = cloudColor.copy(alpha = (0.055f + index * 0.009f) * breathe),
                        radius = radius * (0.72f + lobe * .055f),
                        center = Offset(
                            shiftedX + (lobe - 2) * radius * .58f,
                            y * size.height + sin((lobe + index).toDouble()).toFloat() * radius * .22f,
                        ),
                    )
                }
            }
        }
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, NightBottom.copy(alpha = .54f))))
    }
}

@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(modifier = modifier.fillMaxWidth(), color = CardColor, shape = RoundedCornerShape(20.dp)) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun CardTitle(icon: String, title: String) {
    Text("$icon  $title", color = SecondaryText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
}

@Composable
private fun HourlyForecastCard(summary: String, hourly: List<HourForecast>) {
    WeatherCard {
        Text(summary, color = PrimaryText, fontSize = 20.sp, lineHeight = 25.sp)
        DividerLine(Modifier.padding(top = 16.dp, bottom = 12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(19.dp),
        ) {
            hourly.forEach { hour -> HourColumn(hour) }
        }
    }
}

@Composable
private fun HourColumn(hour: HourForecast) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
        Text(hour.time, color = PrimaryText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        WeatherGlyph(hour.condition, Modifier.padding(vertical = 8.dp).size(38.dp))
        Text(hour.precipitationChance?.let { "$it%" } ?: " ", color = RainBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("${hour.temperature}°", color = PrimaryText, fontSize = 21.sp)
    }
}

@Composable
private fun TenDayForecastCard(days: List<DayForecast>) {
    WeatherCard {
        CardTitle("▦", "10-DAY FORECAST")
        DividerLine(Modifier.padding(top = 12.dp))
        days.forEachIndexed { index, day ->
            ForecastDayRow(day)
            if (index != days.lastIndex) DividerLine()
        }
    }
}

@Composable
private fun ForecastDayRow(day: DayForecast) {
    Row(
        modifier = Modifier.fillMaxWidth().height(62.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(day.day, color = PrimaryText, fontSize = 20.sp, modifier = Modifier.width(72.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(54.dp)) {
            WeatherGlyph(day.condition, Modifier.size(30.dp))
            day.precipitationChance?.let { Text("$it%", color = RainBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
        }
        Text("${day.low}°", color = SecondaryText, fontSize = 20.sp, textAlign = TextAlign.End, modifier = Modifier.width(46.dp))
        TemperatureRangeBar(day.low, day.high, modifier = Modifier.weight(1f).padding(horizontal = 10.dp))
        Text("${day.high}°", color = PrimaryText, fontSize = 20.sp, textAlign = TextAlign.End, modifier = Modifier.width(46.dp))
    }
}

@Composable
private fun TemperatureRangeBar(low: Int, high: Int, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.height(12.dp)) {
        val y = size.height / 2
        drawLine(Color(0xFF202A39), Offset(0f, y), Offset(size.width, y), 5.dp.toPx(), StrokeCap.Round)
        val start = ((low - 65) / 30f).coerceIn(0f, .75f) * size.width
        val end = ((high - 65) / 30f).coerceIn(.25f, 1f) * size.width
        drawLine(
            Brush.horizontalGradient(listOf(Color(0xFFFFD20A), Color(0xFFFF7228))),
            Offset(start, y), Offset(end, y), 5.dp.toPx(), StrokeCap.Round,
        )
    }
}

@Composable
private fun MetricCard(icon: String, title: String, value: String, description: String, modifier: Modifier = Modifier) {
    WeatherCard(modifier = modifier.height(206.dp)) {
        CardTitle(icon, title)
        Text(value, color = PrimaryText, fontSize = 40.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(top = 16.dp))
        Spacer(Modifier.weight(1f))
        Text(description, color = PrimaryText, fontSize = 17.sp, lineHeight = 22.sp)
    }
}

@Composable
private fun UvIndexCard(modifier: Modifier = Modifier) {
    WeatherCard(modifier.height(206.dp)) {
        CardTitle("☀", "UV INDEX")
        Text("0", color = PrimaryText, fontSize = 40.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(top = 16.dp))
        Text("Low", color = PrimaryText, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        SpectrumBar(Modifier.padding(vertical = 8.dp))
        Text("Use sun protection 10AM–6PM.", color = PrimaryText, fontSize = 17.sp, lineHeight = 22.sp)
    }
}

@Composable
private fun SpectrumBar(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxWidth().height(8.dp)) {
        drawLine(
            Brush.horizontalGradient(listOf(Color(0xFF47DC7B), Color.Yellow, Color(0xFFFF7A3D), Color(0xFFFF2A8A), Color(0xFFC426D7))),
            Offset(0f, size.height / 2), Offset(size.width, size.height / 2), 4.dp.toPx(), StrokeCap.Round,
        )
        drawCircle(PrimaryText, 4.dp.toPx(), Offset(4.dp.toPx(), size.height / 2))
    }
}

@Composable
private fun WindCard() {
    WeatherCard {
        CardTitle("≋", "WIND")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                MetricRow("Wind", "5 mph")
                DividerLine()
                MetricRow("Gusts", "15 mph")
                DividerLine()
                MetricRow("Direction", "195° SSW")
            }
            Compass(Modifier.size(132.dp))
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = PrimaryText, fontSize = 17.sp)
        Text(value, color = SecondaryText, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Compass(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val outer = size.minDimension * .43f
        repeat(48) { index ->
            val angle = index * 2 * PI / 48 - PI / 2
            val start = Offset(center.x + cos(angle).toFloat() * outer * .82f, center.y + sin(angle).toFloat() * outer * .82f)
            val end = Offset(center.x + cos(angle).toFloat() * outer, center.y + sin(angle).toFloat() * outer)
            drawLine(SecondaryText.copy(alpha = .28f), start, end, if (index % 4 == 0) 2.dp.toPx() else 1.dp.toPx())
        }
        drawCircle(Color(0x16000000), outer * .72f, center)
        drawLine(PrimaryText, center, Offset(center.x + 10.dp.toPx(), center.y - outer * .8f), 4.dp.toPx(), StrokeCap.Round)
        drawCircle(PrimaryText, 6.dp.toPx(), Offset(center.x - 8.dp.toPx(), center.y + outer * .68f))
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        Text("5\nmph", color = PrimaryText, fontSize = 18.sp, lineHeight = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun SunriseCard(modifier: Modifier = Modifier) {
    WeatherCard(modifier.height(206.dp)) {
        CardTitle("☀", "SUNRISE")
        Text("6:09AM", color = PrimaryText, fontSize = 36.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(top = 16.dp))
        Canvas(Modifier.fillMaxWidth().weight(1f)) {
            val path = Path().apply {
                moveTo(0f, size.height)
                cubicTo(size.width * .25f, size.height, size.width * .35f, 0f, size.width * .5f, 0f)
                cubicTo(size.width * .65f, 0f, size.width * .75f, size.height, size.width, size.height)
            }
            drawPath(path, SecondaryText.copy(alpha = .65f), style = Stroke(4.dp.toPx()))
            drawLine(SecondaryText, Offset(0f, size.height * .62f), Offset(size.width, size.height * .62f), 1.dp.toPx())
        }
        Text("Sunset: 8:16PM", color = PrimaryText, fontSize = 16.sp)
    }
}

@Composable
private fun AirQualityCard() {
    WeatherCard {
        CardTitle("⠿", "AIR QUALITY")
        Text("36", color = PrimaryText, fontSize = 42.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(top = 12.dp))
        Text("Good", color = PrimaryText, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        SpectrumBar(Modifier.padding(vertical = 12.dp))
        Text("Air quality index is 36, which is similar to yesterday at about this time.", color = PrimaryText, fontSize = 17.sp, lineHeight = 22.sp)
    }
}

@Composable
private fun WeatherAlertCard() {
    WeatherCard {
        CardTitle("⚠", "WEATHER ALERT")
        Text("Coastal Flood Advisory", color = PrimaryText, fontSize = 23.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        Text("These conditions are expected to last until 10:00 AM, Monday, August 3.", color = PrimaryText, fontSize = 17.sp, lineHeight = 23.sp, modifier = Modifier.padding(top = 8.dp))
        Text("National Weather Service", color = SecondaryText, fontSize = 15.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun FooterCard() {
    WeatherCard {
        Text("!   Report an Issue", color = PrimaryText, fontSize = 21.sp, fontWeight = FontWeight.SemiBold)
        Text("You can describe the current conditions at your location to help improve forecasts.", color = SecondaryText, fontSize = 17.sp, lineHeight = 22.sp, modifier = Modifier.padding(start = 24.dp, top = 4.dp))
        DividerLine(Modifier.padding(vertical = 14.dp))
        Text("Weather for Home", color = PrimaryText, fontSize = 18.sp)
        Text("Phoenix, Arizona", color = SecondaryText, fontSize = 16.sp)
    }
}

@Composable
private fun WeatherGlyph(condition: WeatherCondition, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        when (condition) {
            WeatherCondition.Clear -> {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(Color(0xFFFFD70A), size.minDimension * .21f, center)
                repeat(8) { index ->
                    val angle = index * PI / 4
                    drawLine(
                        Color(0xFFFFD70A),
                        Offset(center.x + cos(angle).toFloat() * size.width * .31f, center.y + sin(angle).toFloat() * size.height * .31f),
                        Offset(center.x + cos(angle).toFloat() * size.width * .43f, center.y + sin(angle).toFloat() * size.height * .43f),
                        2.dp.toPx(), StrokeCap.Round,
                    )
                }
            }
            else -> {
                drawCircle(PrimaryText, size.width * .19f, Offset(size.width * .38f, size.height * .43f))
                drawCircle(PrimaryText, size.width * .25f, Offset(size.width * .57f, size.height * .36f))
                drawCircle(PrimaryText, size.width * .18f, Offset(size.width * .76f, size.height * .47f))
                drawRoundRect(PrimaryText, Offset(size.width * .18f, size.height * .43f), Size(size.width * .72f, size.height * .28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * .14f))
                if (condition == WeatherCondition.Rain || condition == WeatherCondition.Thunderstorm) {
                    repeat(3) { index ->
                        val x = size.width * (.34f + index * .2f)
                        drawLine(RainBlue, Offset(x, size.height * .78f), Offset(x - size.width * .07f, size.height), 2.dp.toPx(), StrokeCap.Round)
                    }
                }
                if (condition == WeatherCondition.Thunderstorm) {
                    val bolt = Path().apply {
                        moveTo(size.width * .56f, size.height * .66f)
                        lineTo(size.width * .46f, size.height * .86f)
                        lineTo(size.width * .57f, size.height * .84f)
                        lineTo(size.width * .48f, size.height)
                    }
                    drawPath(bolt, Color(0xFFFFE552), style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
                }
            }
        }
    }
}

@Composable
private fun DividerLine(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(1.dp).background(Divider))
}

private fun sampleCloudyNightWeather() = WeatherDetailUiState(
    location = "Phoenix",
    temperature = 74,
    conditionLabel = "Cloudy",
    condition = WeatherCondition.Cloudy,
    high = 86,
    low = 73,
    isNight = true,
    summary = "Thunderstorms expected around 1AM. Wind gusts are up to 15 mph.",
    hourly = listOf(
        HourForecast("Now", 74, WeatherCondition.Cloudy),
        HourForecast("1AM", 74, WeatherCondition.Thunderstorm, 50),
        HourForecast("2AM", 74, WeatherCondition.Thunderstorm, 70),
        HourForecast("3AM", 73, WeatherCondition.Rain, 50),
        HourForecast("4AM", 73, WeatherCondition.Cloudy),
        HourForecast("5AM", 73, WeatherCondition.Cloudy),
        HourForecast("6AM", 74, WeatherCondition.Cloudy),
    ),
    daily = listOf(
        DayForecast("Today", 73, 86, WeatherCondition.Thunderstorm, 95),
        DayForecast("Tue", 69, 86, WeatherCondition.Cloudy),
        DayForecast("Wed", 72, 85, WeatherCondition.Rain, 75),
        DayForecast("Thu", 74, 91, WeatherCondition.Rain, 50),
        DayForecast("Fri", 75, 90, WeatherCondition.Rain, 45),
        DayForecast("Sat", 73, 90, WeatherCondition.Rain, 50),
        DayForecast("Sun", 73, 88, WeatherCondition.Rain, 55),
        DayForecast("Mon", 73, 92, WeatherCondition.Clear),
        DayForecast("Tue", 75, 94, WeatherCondition.Clear),
        DayForecast("Wed", 74, 89, WeatherCondition.Rain, 35),
    ),
)

@Preview(name = "Weather detail · cloudy night", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun WeatherDetailCloudyNightPreview() {
    MaterialTheme { WeatherDetailScreen() }
}

@Preview(name = "Animated header · cloudy night", widthDp = 393, heightDp = 430, showBackground = true)
@Composable
private fun CloudyNightHeaderPreview() {
    val state = remember { sampleCloudyNightWeather() }
    Box(Modifier.fillMaxSize()) {
        AnimatedWeatherBackground(state.condition, state.isNight, Modifier.fillMaxSize())
        CurrentConditionsHeader(state, Modifier.align(Alignment.Center))
    }
}

@Preview(name = "Forecast cards", widthDp = 393, heightDp = 720, showBackground = true)
@Composable
private fun ForecastCardsPreview() {
    val state = remember { sampleCloudyNightWeather() }
    Column(
        Modifier.fillMaxSize().background(NightBottom).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HourlyForecastCard(state.summary, state.hourly)
        TenDayForecastCard(state.daily.take(5))
    }
}
