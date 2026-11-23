package com.example.model

enum class AppTab {
    HOME, TOUR_3D, RITUALS, MAP, MORE
}

enum class AppLanguage(
    val code: String,
    val label: String,
    val isRtl: Boolean
) {
    AR("ar", "🌐 عربي", true),
    EN("en", "🌐 EN", false),
    FR("fr", "🌐 FR", false),
    UR("ur", "🌐 اردو", true),
    ID("id", "🌐 ID", false),
    TR("tr", "🌐 TR", false);

    companion object {
        fun fromCode(code: String): AppLanguage = entries.find { it.code == code } ?: AR
    }
}

data class LocalizedLabels(
    val title: String,
    val subtitle: String,
    val navHome: String,
    val nav3D: String,
    val navRituals: String,
    val navMap: String,
    val navMore: String,
    val heroButton: String
)

enum class PrayerCity(val id: String, val label: String) {
    MAKKAH("makkah", "📍 مكة المكرمة"),
    MADINAH("madinah", "🕌 المدينة المنورة"),
    GPS("gps", "📡 موقعي (GPS)")
}

enum class WeatherCity(
    val id: String,
    val shortLabel: String,
    val fullName: String,
    val lat: Double,
    val lng: Double
) {
    MAKKAH("makkah", "مكة", "مكة المكرمة", 21.4225, 39.8262),
    MADINAH("madinah", "المدينة", "المدينة المنورة", 24.4672, 39.6112),
    ARAFAT("arafat", "عرفات", "مشعر عرفات", 21.3549, 39.9841),
    MY_GPS("gps", "موقعي", "موقعي (GPS)", 21.4225, 39.8262)
}

enum class HeatDangerLevel {
    SAFE, CAUTION, DANGER, EXTREME
}

data class WeatherState(
    val city: WeatherCity = WeatherCity.MAKKAH,
    val lat: Double = 21.4225,
    val lng: Double = 39.8262,
    val temperatureC: Int = 30,
    val feelsLikeC: Int = 31,
    val highC: Int = 34,
    val lowC: Int = 21,
    val humidity: Int = 42,
    val windKmh: Int = 12,
    val uvIndex: Int = 8,
    val dangerLevel: HeatDangerLevel = HeatDangerLevel.SAFE,
    val description: String = "مكة المكرمة: صافٍ ومشمس",
    val advice: String = "أجواء مناسبة للمناسك",
    val iconEmoji: String = "☀️",
    val isLoading: Boolean = true,
    val isEstimated: Boolean = false
)

data class PrayerEntry(
    val name: String,
    val formattedTime: String
)

data class PrayerSchedule(
    val nextName: String = "الفجر",
    val nextTimeFormatted: String = "--:--",
    val countdownFormatted: String = "--:--:--",
    val allPrayers: List<PrayerEntry> = emptyList(),
    val lastThirdFormatted: String = "--:--"
)

enum class Scene3DType(val id: String, val chipLabel: String) {
    KAABA("kaaba", "🕋 الكعبة"),
    MASAA("masaa", "🏃 المسعى"),
    ARAFAT("arafat", "⛰️ عرفة"),
    JAMARAT("jamarat", "🪨 منى والجمرات"),
    MADINAH("madinah", "🕌 المسجد النبوي")
}

data class Scene3DInfo(
    val type: Scene3DType,
    val title: String,
    val description: String,
    val tags: List<String>
)

data class Scene3DLabels(
    val blackStone: String,
    val yemeniCorner: String,
    val maqamIbrahim: String,
    val safa: String,
    val marwah: String,
    val greenMarkers: String,
    val jabalRahmah: String,
    val masjidNamirah: String,
    val jamaratSughra: String,
    val jamaratWusta: String,
    val jamaratKubra: String,
    val greenDome: String,
    val rawda: String,
    val busRouteLabel: String,
    val busStationLabel: String,
    val ambulanceHubLabel: String,
    val helipadLabel: String,
    val waterMistLabel: String,
    val trainStationLabel: String,
    val servicesCenterLabel: String,
    val cartsLaneLabel: String
)

enum class NusukType(val id: String, val label: String, val guideTitle: String) {
    UMRAH("umrah", "🕋 العمرة", "مسار العمرة المصوّر"),
    TAMATTU("tamattu", "⛺ التمتع", "مسار حج التمتع المصوّر"),
    IFRAD("ifrad", "الإفراد", "مسار حج الإفراد المصوّر"),
    QIRAN("qiran", "القران", "مسار حج القران المصوّر"),
    MADINAH("madinah", "🕌 المدينة", "دليل زيارة المدينة المصوّر")
}

enum class CounterMode(val id: String, val label: String) {
    TAWAF("tawaf", "الطواف (7)"),
    SAI("sai", "السعي (7)"),
    JAMARAT("jamarat", "الجمرات (21)")
}

enum class TawafFloor(val minutes: Int, val label: String) {
    SAHN(20, "صحن الطواف (~20 د)"),
    GROUND(35, "الدور الأرضي (~35 د)"),
    FIRST(45, "الدور الأول (~45 د)"),
    ROOF(55, "السطح (~55 د - أوسع)")
}

enum class RitualBadgeType(val label: String) {
    RUKN("ركن"),
    WAJIB("واجب"),
    SUNNAH("سنة")
}

enum class StepIllustrationKey {
    IHRAM, TAWAF, SAI, HALQ, MINA, ARAFAT, MUZDALIFAH, JAMARAT, MADINAH
}

data class RitualStep(
    val id: String,
    val badgeType: RitualBadgeType,
    val title: String,
    val description: String,
    val scene3D: Scene3DType,
    val illustrationKey: StepIllustrationKey,
    val visualCaption: String = "",
    val detailedPoints: List<String> = emptyList(),
    val stageDua: String = "",
    val locationHint: String = ""
)

data class PilgrimProfile(
    val name: String = "عبدالله محمد العتيبي",
    val nationality: String = "سعودي",
    val bloodType: String = "O+",
    val medicalConditions: String = "لا يوجد",
    val mutawwifPhone: String = "0500000000"
)

data class DuaEntry(
    val title: String,
    val text: String
)

enum class MoreTextTab(val label: String) {
    DUAS("الأدعية"),
    QURAN("المصحف المختصر"),
    ADHKAR("الأذكار")
}

data class FaqEntry(
    val question: String,
    val answer: String
)

data class PhraseEntry(
    val arabic: String,
    val english: String
)

data class ChecklistTemplate(
    val id: String,
    val label: String,
    val defaultChecked: Boolean
)

enum class ActiveModal {
    NONE,
    ABOUT,
    CURRENCY,
    MISTAKES,
    WOMEN,
    FIDYAH,
    LOVED_ONES,
    AI_FAQ,
    IHRAM,
    ID_CARD,
    TRANSLATOR,
    SHOE,
    HOTEL,
    SOS,
    PROFILE,
    STEP_PREVIEW
}

enum class CompassAccuracy {
    HIGH, MEDIUM, LOW, UNRELIABLE
}

enum class QiblaLocationSource {
    NONE, GPS, SAVED, CITY
}

enum class QiblaReferenceCity(
    val arName: String,
    val enName: String,
    val lat: Double,
    val lng: Double
) {
    MADINAH("المدينة المنورة", "Madinah", 24.4672, 39.6112),
    RIYADH("الرياض", "Riyadh", 24.7136, 46.6753),
    CAIRO("القاهرة", "Cairo", 30.0444, 31.2357),
    ISTANBUL("إسطنبول", "Istanbul", 41.0082, 28.9784),
    JAKARTA("جاكرتا", "Jakarta", -6.2088, 106.8456),
    LONDON("لندن", "London", 51.5072, -0.1276)
}

data class CompassReading(
    val magneticAzimuthDeg: Float = 0f,
    val trueAzimuthDeg: Float = 0f,
    val pitchDeg: Float = 0f,
    val rollDeg: Float = 0f,
    val magneticFieldMicroTesla: Float = 0f,
    val declinationDeg: Float = 0f,
    val accuracy: CompassAccuracy = CompassAccuracy.MEDIUM,
    val hasAccelerometer: Boolean = true,
    val hasMagnetometer: Boolean = true,
    val hasInitialReading: Boolean = false
)

