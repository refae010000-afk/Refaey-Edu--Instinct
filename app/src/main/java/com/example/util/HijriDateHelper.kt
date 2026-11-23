package com.example.util

import android.os.Build
import java.time.chrono.HijrahChronology
import java.time.temporal.ChronoField
import java.util.Calendar
import java.util.TimeZone

object HijriDateHelper {
    private val arabicMonths = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    fun todayHijriFormatted(nowMillis: Long = System.currentTimeMillis()): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return try {
                val zone = java.time.ZoneId.of("Asia/Riyadh")
                val instant = java.time.Instant.ofEpochMilli(nowMillis)
                val localDate = instant.atZone(zone).toLocalDate()
                val hijrah = HijrahChronology.INSTANCE.date(localDate)
                val day = hijrah.get(ChronoField.DAY_OF_MONTH)
                val monthIdx = (hijrah.get(ChronoField.MONTH_OF_YEAR) - 1).coerceIn(0, 11)
                val year = hijrah.get(ChronoField.YEAR)
                "$day ${arabicMonths[monthIdx]} $year هـ"
            } catch (_: Exception) {
                fallbackKuwaiti(nowMillis)
            }
        }
        return fallbackKuwaiti(nowMillis)
    }

    private fun fallbackKuwaiti(nowMillis: Long): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3")).apply {
            timeInMillis = nowMillis
        }
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH) + 1
        val y = cal.get(Calendar.YEAR)
        val jd = (1461 * (y + 4800 + (m - 14) / 12)) / 4 +
            (367 * (m - 2 - 12 * ((m - 14) / 12))) / 12 -
            (3 * ((y + 4900 + (m - 14) / 12) / 100)) / 4 + d - 32075
        val l = jd - 1948440 + 10632
        val n = (l - 1) / 10631
        val l2 = l - 10631 * n + 354
        val j = ((10985 - l2) / 5316) * ((50 * l2) / 17719) + (l2 / 5670) * ((43 * l2) / 15238)
        val l3 = l2 - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
        val hm = (24 * l3) / 709
        val hd = l3 - (709 * hm) / 24
        val hy = 30 * n + j - 30
        val monthName = arabicMonths.getOrElse(hm - 1) { arabicMonths[0] }
        return "$hd $monthName $hy هـ"
    }
}
