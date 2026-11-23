package com.example.util

import com.example.model.HeatDangerLevel
import kotlin.math.roundToInt

object HeatAlertEvaluator {

    /**
     * Rothfusz / NOAA heat index regression in Celsius for warm temperatures (>= 27°C).
     */
    fun computeHeatIndexC(tempC: Int, humidityPct: Int): Int {
        if (tempC < 27) return tempC
        val tF = tempC * 9.0 / 5.0 + 32.0
        val rh = humidityPct.coerceIn(0, 100).toDouble()
        val hiF = -42.379 +
            2.04901523 * tF +
            10.14333127 * rh -
            0.22475541 * tF * rh -
            0.00683783 * tF * tF -
            0.05481717 * rh * rh +
            0.00122874 * tF * tF * rh +
            0.00085282 * tF * rh * rh -
            0.00000199 * tF * tF * rh * rh
        val hiC = ((hiF - 32.0) * 5.0 / 9.0).roundToInt()
        return maxOf(tempC, hiC)
    }

    fun evaluateDanger(
        tempC: Int,
        feelsLikeC: Int,
        uvIndex: Int,
        dangerThresholdC: Int = 40
    ): HeatDangerLevel {
        val effectiveTemp = maxOf(tempC, feelsLikeC - 2)
        return when {
            tempC >= dangerThresholdC + 4 || feelsLikeC >= dangerThresholdC + 6 -> HeatDangerLevel.EXTREME
            effectiveTemp >= dangerThresholdC -> HeatDangerLevel.DANGER
            tempC >= (dangerThresholdC - 5).coerceAtLeast(33) || uvIndex >= 9 -> HeatDangerLevel.CAUTION
            else -> HeatDangerLevel.SAFE
        }
    }

    fun shouldPushAlert(
        dangerLevel: HeatDangerLevel,
        tempC: Int,
        feelsLikeC: Int,
        dangerThresholdC: Int
    ): Boolean {
        return dangerLevel == HeatDangerLevel.DANGER ||
            dangerLevel == HeatDangerLevel.EXTREME ||
            tempC >= dangerThresholdC ||
            feelsLikeC >= dangerThresholdC + 2
    }
}
