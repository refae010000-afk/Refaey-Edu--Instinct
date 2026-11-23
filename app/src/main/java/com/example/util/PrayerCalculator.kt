package com.example.util

import com.example.model.PrayerEntry
import com.example.model.PrayerSchedule
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

object PrayerCalculator {

    fun calculate(latDeg: Double, lngDeg: Double, nowMillis: Long = System.currentTimeMillis()): PrayerSchedule {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3")).apply {
            timeInMillis = nowMillis
        }
        val doy = cal.get(Calendar.DAY_OF_YEAR)

        fun rad(d: Double) = d * PI / 180.0
        fun deg(r: Double) = r * 180.0 / PI

        val b = rad((360.0 / 365.0) * (doy - 81))
        val eot = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b)
        val dec = rad(23.45 * sin(b))
        val lat = rad(latDeg)
        val noon = 12.0 + (45.0 - lngDeg) / 15.0 - eot / 60.0

        fun ha(altDeg: Double): Double {
            val cosVal = ((sin(rad(altDeg)) - sin(lat) * sin(dec)) / (cos(lat) * cos(dec)))
                .coerceIn(-1.0, 1.0)
            return deg(acos(cosVal)) / 15.0
        }

        val fajr = noon - ha(-18.5)
        val sunrise = noon - ha(-0.833)
        val dhuhr = noon
        val asrAlt = deg(atan(1.0 / (1.0 + tan(abs(lat - dec)))))
        val asr = noon + ha(asrAlt)
        val maghrib = noon + ha(-0.833)
        val isha = maghrib + 1.5

        val list = listOf(
            "الفجر" to fajr,
            "الشروق" to sunrise,
            "الظهر" to dhuhr,
            "العصر" to asr,
            "المغرب" to maghrib,
            "العشاء" to isha
        )

        val curH = cal.get(Calendar.HOUR_OF_DAY) +
            cal.get(Calendar.MINUTE) / 60.0 +
            cal.get(Calendar.SECOND) / 3600.0

        val nextPair = list.firstOrNull { it.second > curH }
        val (nextName, nextHour, diffSec) = if (nextPair != null) {
            Triple(nextPair.first, nextPair.second, ((nextPair.second - curH) * 3600.0).toInt().coerceAtLeast(0))
        } else {
            val first = list.first()
            Triple(first.first, first.second, (((24.0 - curH) + first.second) * 3600.0).toInt().coerceAtLeast(0))
        }

        fun fmt(h: Double): String {
            val totalMin = (h * 60.0).roundToInt()
            val h24 = Math.floorMod(floor(totalMin / 60.0).toInt(), 24)
            val mn = Math.floorMod(totalMin, 60)
            val h12 = if (h24 % 12 == 0) 12 else h24 % 12
            val suffix = if (h24 >= 12) "م" else "ص"
            return "${h12.toString().padStart(2, '0')}:${mn.toString().padStart(2, '0')} $suffix"
        }

        val lastThird = ((fajr - ((24.0 - maghrib + fajr) / 3.0)) % 24.0 + 24.0) % 24.0
        val hh = (diffSec / 3600).toString().padStart(2, '0')
        val mm = ((diffSec % 3600) / 60).toString().padStart(2, '0')
        val ss = (diffSec % 60).toString().padStart(2, '0')

        return PrayerSchedule(
            nextName = nextName,
            nextTimeFormatted = fmt(nextHour),
            countdownFormatted = "$hh:$mm:$ss",
            allPrayers = list.map { PrayerEntry(it.first, fmt(it.second)) },
            lastThirdFormatted = fmt(lastThird)
        )
    }
}
