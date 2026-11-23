package com.example.data

import com.example.model.WeatherCity
import com.example.model.WeatherState
import com.example.util.HeatAlertEvaluator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    val current: CurrentWeatherDto? = null,
    val daily: DailyWeatherDto? = null
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @Json(name = "temperature_2m") val temperature2m: Double = 30.0,
    @Json(name = "apparent_temperature") val apparentTemperature: Double? = null,
    @Json(name = "relative_humidity_2m") val relativeHumidity2m: Int = 40,
    @Json(name = "wind_speed_10m") val windSpeed10m: Double = 12.0,
    @Json(name = "weather_code") val weatherCode: Int = 0
)

@JsonClass(generateAdapter = true)
data class DailyWeatherDto(
    @Json(name = "temperature_2m_max") val temperature2mMax: List<Double> = emptyList(),
    @Json(name = "temperature_2m_min") val temperature2mMin: List<Double> = emptyList(),
    @Json(name = "uv_index_max") val uvIndexMax: List<Double> = emptyList()
)

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,uv_index_max",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoResponse
}

class WeatherRepository {
    private val api: OpenMeteoApi by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OpenMeteoApi::class.java)
    }

    suspend fun fetchWeather(city: WeatherCity, dangerThresholdC: Int = 40): WeatherState {
        return fetchWeatherByCoordinates(
            lat = city.lat,
            lng = city.lng,
            city = city,
            dangerThresholdC = dangerThresholdC
        )
    }

    suspend fun fetchWeatherByCoordinates(
        lat: Double,
        lng: Double,
        city: WeatherCity = WeatherCity.MY_GPS,
        dangerThresholdC: Int = 40
    ): WeatherState {
        return try {
            val res = api.getForecast(latitude = lat, longitude = lng)
            val cur = res.current ?: return fallbackWeather(city, lat, lng, dangerThresholdC)
            val dl = res.daily
            val code = cur.weatherCode
            val temp = cur.temperature2m.roundToInt()
            val humidity = cur.relativeHumidity2m
            val computedHi = HeatAlertEvaluator.computeHeatIndexC(temp, humidity)
            val feelsLike = maxOf(cur.apparentTemperature?.roundToInt() ?: temp, computedHi)
            val high = dl?.temperature2mMax?.firstOrNull()?.roundToInt() ?: (temp + 4)
            val low = dl?.temperature2mMin?.firstOrNull()?.roundToInt() ?: (temp - 7)
            val uv = dl?.uvIndexMax?.firstOrNull()?.roundToInt() ?: 7
            val danger = HeatAlertEvaluator.evaluateDanger(temp, feelsLike, uv, dangerThresholdC)
            val hot = temp >= 34

            val icon = when {
                code <= 1 -> "☀️"
                code <= 3 -> "⛅"
                else -> "🌦️"
            }
            val cond = when {
                code <= 1 -> "صافٍ ومشمس"
                code <= 3 -> "غائم جزئياً"
                else -> "محتمل أمطار"
            }
            WeatherState(
                city = city,
                lat = lat,
                lng = lng,
                temperatureC = temp,
                feelsLikeC = feelsLike,
                highC = high,
                lowC = low,
                humidity = humidity,
                windKmh = cur.windSpeed10m.roundToInt(),
                uvIndex = uv,
                dangerLevel = danger,
                description = "${city.fullName}: $cond",
                advice = if (hot) "حار نهاراً: استخدم المظلة واشرب ماء زمزم" else "أجواء مناسبة للمناسك",
                iconEmoji = icon,
                isLoading = false,
                isEstimated = false
            )
        } catch (_: Exception) {
            fallbackWeather(city, lat, lng, dangerThresholdC)
        }
    }

    private fun fallbackWeather(
        city: WeatherCity,
        lat: Double,
        lng: Double,
        dangerThresholdC: Int
    ): WeatherState {
        val (t, h, l, hum) = when (city) {
            WeatherCity.MAKKAH -> listOf(30, 34, 21, 42)
            WeatherCity.MADINAH -> listOf(26, 30, 17, 35)
            WeatherCity.ARAFAT -> listOf(31, 35, 20, 38)
            WeatherCity.MY_GPS -> listOf(30, 34, 21, 40)
        }
        val feels = HeatAlertEvaluator.computeHeatIndexC(t, hum)
        val danger = HeatAlertEvaluator.evaluateDanger(t, feels, 7, dangerThresholdC)
        return WeatherState(
            city = city,
            lat = lat,
            lng = lng,
            temperatureC = t,
            feelsLikeC = feels,
            highC = h,
            lowC = l,
            humidity = hum,
            windKmh = 12,
            uvIndex = 7,
            dangerLevel = danger,
            description = "${city.fullName} (تقديري)",
            advice = "اشرب الماء بانتظام واستخدم المظلة",
            iconEmoji = "☀️",
            isLoading = false,
            isEstimated = true
        )
    }
}
