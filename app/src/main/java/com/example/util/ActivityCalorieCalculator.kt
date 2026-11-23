package com.example.util

import kotlin.math.roundToInt

data class RitualActivityStats(
    val tawafRounds: Int,
    val saiRounds: Int,
    val tawafDistanceKm: Double,
    val saiDistanceKm: Double,
    val totalDistanceKm: Double,
    val tawafCalories: Int,
    val saiCalories: Int,
    val totalCalories: Int,
    val estimatedSteps: Int
)

object ActivityCalorieCalculator {
    const val TAWAF_ROUND_KM = 0.25
    const val SAI_ROUND_KM = 0.40

    fun calculate(
        tawafRounds: Int,
        saiRounds: Int,
        totalKm: Double,
        weightKg: Int
    ): RitualActivityStats {
        val safeTawaf = tawafRounds.coerceAtLeast(0)
        val safeSai = saiRounds.coerceAtLeast(0)
        val safeWeight = weightKg.coerceIn(35, 180)

        val tawafKm = safeTawaf * TAWAF_ROUND_KM
        val saiKm = safeSai * SAI_ROUND_KM
        val combinedRoundKm = tawafKm + saiKm
        val effectiveTotalKm = maxOf(totalKm, combinedRoundKm)

        val tawafCal = (tawafKm * safeWeight * 0.75).roundToInt()
        val saiCal = (saiKm * safeWeight * 0.85).roundToInt()

        // Include any residual distance if legacy totalKm exceeded round-based km
        val residualKm = (effectiveTotalKm - combinedRoundKm).coerceAtLeast(0.0)
        val residualCal = (residualKm * safeWeight * 0.78).roundToInt()

        val totalCal = tawafCal + saiCal + residualCal
        val steps = (effectiveTotalKm * 1350.0).roundToInt()

        return RitualActivityStats(
            tawafRounds = safeTawaf,
            saiRounds = safeSai,
            tawafDistanceKm = tawafKm,
            saiDistanceKm = saiKm,
            totalDistanceKm = effectiveTotalKm,
            tawafCalories = tawafCal,
            saiCalories = saiCal,
            totalCalories = totalCal,
            estimatedSteps = steps
        )
    }
}
