package com.example.util

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.example.data.PilgrimPreferencesRepository
import com.example.data.WeatherRepository
import com.example.model.WeatherCity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object WeatherJobScheduler {
    private const val JOB_ID = 2026

    fun schedulePeriodicMonitor(context: Context) {
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler ?: return
        val component = ComponentName(context, WeatherMonitorJobService::class.java)
        val info = JobInfo.Builder(JOB_ID, component)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPeriodic(30L * 60L * 1000L) // 30-minute coordinate weather monitoring
            .setPersisted(false)
            .build()
        scheduler.schedule(info)
    }

    fun cancelMonitor(context: Context) {
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler ?: return
        scheduler.cancel(JOB_ID)
    }
}

class WeatherMonitorJobService : JobService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onStartJob(params: JobParameters?): Boolean {
        serviceScope.launch {
            var shouldReschedule = false
            try {
                val repo = PilgrimPreferencesRepository(applicationContext)
                val stored = repo.dataFlow.first()
                if (!stored.weatherAlertsEnabled) {
                    jobFinished(params, false)
                    return@launch
                }

                val locHelper = LocationAndSensorHelper(applicationContext)
                val freshPoint = if (locHelper.hasLocationPermission()) {
                    locHelper.getCurrentLocation()
                } else null

                val lat = freshPoint?.lat ?: stored.lastUserLat ?: WeatherCity.MAKKAH.lat
                val lng = freshPoint?.lng ?: stored.lastUserLng ?: WeatherCity.MAKKAH.lng

                if (freshPoint != null) {
                    repo.saveLastUserCoordinates(freshPoint.lat, freshPoint.lng)
                }

                val weather = WeatherRepository().fetchWeatherByCoordinates(
                    lat = lat,
                    lng = lng,
                    city = WeatherCity.MY_GPS,
                    dangerThresholdC = stored.weatherDangerThresholdC
                )

                val shouldAlert = HeatAlertEvaluator.shouldPushAlert(
                    dangerLevel = weather.dangerLevel,
                    tempC = weather.temperatureC,
                    feelsLikeC = weather.feelsLikeC,
                    dangerThresholdC = stored.weatherDangerThresholdC
                )

                val nowMs = System.currentTimeMillis()
                val twoHoursMs = 2L * 60L * 60L * 1000L
                val cooldownElapsed = (nowMs - stored.lastHeatAlertMs) >= twoHoursMs

                if (shouldAlert && cooldownElapsed) {
                    val sent = WeatherAlertNotifier.showHeatAlert(
                        context = applicationContext,
                        weather = weather,
                        language = stored.language,
                        thresholdC = stored.weatherDangerThresholdC
                    )
                    if (sent) {
                        repo.saveLastHeatAlertMs(nowMs)
                    }
                }
            } catch (_: Exception) {
                shouldReschedule = true
            } finally {
                jobFinished(params, shouldReschedule)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        serviceScope.cancel()
        return true
    }
}
