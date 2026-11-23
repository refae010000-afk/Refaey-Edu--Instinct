package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.GlobalStrings
import com.example.model.AppLanguage
import com.example.model.HeatDangerLevel
import com.example.model.WeatherState
import java.util.Locale

object WeatherAlertNotifier {
    const val CHANNEL_ID = "weather_heat_alerts"
    private const val NOTIFICATION_ID = 4102

    fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun ensureChannel(context: Context, language: AppLanguage = AppLanguage.AR) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val strings = GlobalStrings.forLang(language)
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
            val existing = nm.getNotificationChannel(CHANNEL_ID)
            if (existing == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    strings.weatherChannelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = strings.weatherChannelDesc
                    enableVibration(true)
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    fun showHeatAlert(
        context: Context,
        weather: WeatherState,
        language: AppLanguage,
        thresholdC: Int,
        isTestPreview: Boolean = false
    ): Boolean {
        if (!hasNotificationPermission(context)) return false
        ensureChannel(context, language)

        val strings = GlobalStrings.forLang(language)
        val levelLabel = when (weather.dangerLevel) {
            HeatDangerLevel.EXTREME -> strings.heatExtremeLabel
            HeatDangerLevel.DANGER -> strings.heatDangerLabel
            HeatDangerLevel.CAUTION -> strings.heatCautionLabel
            HeatDangerLevel.SAFE -> strings.heatSafeLabel
        }

        val coordsStr = String.format(Locale.US, "%.3f, %.3f", weather.lat, weather.lng)
        val title = if (isTestPreview) {
            "🔔 ${strings.heatAlertPushTitle}: ${weather.temperatureC}°C ($levelLabel)"
        } else {
            "⚠️ ${strings.heatAlertPushTitle}: ${weather.temperatureC}°C ($levelLabel)"
        }

        val body = "${strings.heatFeelsLikePrefix} ${weather.feelsLikeC}°C • UV ${weather.uvIndex} • " +
            "${strings.heatThresholdPrefix} $thresholdC°C ($coordsStr). ${strings.weatherAdviceHot}"

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        return try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            true
        } catch (_: SecurityException) {
            false
        }
    }
}
