package com.example.data

import com.example.R
import com.example.model.AppLanguage
import com.example.model.LandmarkCategory
import com.example.model.OfflineMapRegion
import com.example.model.OfflineMapRoute
import com.example.model.OfflineMapStrings
import com.example.model.SacredLandmark
import com.example.model.Scene3DType
import com.example.util.GeoMath
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

object OfflineMapMath {
    fun project(lat: Double, lng: Double, region: OfflineMapRegion): Pair<Float, Float> {
        val latSpan = (region.maxLat - region.minLat).coerceAtLeast(1e-6)
        val lngSpan = (region.maxLng - region.minLng).coerceAtLeast(1e-6)
        val x = ((lng - region.minLng) / lngSpan).toFloat().coerceIn(0.03f, 0.97f)
        val y = (1.0 - ((lat - region.minLat) / latSpan)).toFloat().coerceIn(0.05f, 0.95f)
        return x to y
    }

    fun isWithinRegion(lat: Double, lng: Double, region: OfflineMapRegion): Boolean {
        return lat in region.minLat..region.maxLat && lng in region.minLng..region.maxLng
    }

    fun bearingDegrees(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val l1 = lat1 * PI / 180.0
        val n1 = lng1 * PI / 180.0
        val l2 = lat2 * PI / 180.0
        val n2 = lng2 * PI / 180.0
        val y = sin(n2 - n1) * cos(l2)
        val x = cos(l1) * sin(l2) - sin(l1) * cos(l2) * cos(n2 - n1)
        return ((atan2(y, x) * 180.0 / PI) + 360.0) % 360.0
    }

    fun formatDistance(meters: Double, strings: OfflineMapStrings): String {
        return if (meters < 1000.0) {
            "${meters.roundToInt()} ${strings.mUnit}"
        } else {
            "${String.format(Locale.US, "%.2f", meters / 1000.0)} ${strings.kmUnit}"
        }
    }

    fun distanceFromRegionHubMeters(landmark: SacredLandmark): Double {
        val (hubLat, hubLng) = when (landmark.region) {
            OfflineMapRegion.MADINAH_SANCTUARY -> 24.4672 to 39.6112
            else -> GeoMath.KAABA_LAT to GeoMath.KAABA_LNG
        }
        return GeoMath.distanceMeters(hubLat, hubLng, landmark.lat, landmark.lng)
    }
}

internal data class LandmarkGeoSpec(
    val id: String,
    val region: OfflineMapRegion,
    val category: LandmarkCategory,
    val lat: Double,
    val lng: Double,
    val photoRes: Int,
    val scene3D: Scene3DType? = null,
    val isPrimaryHub: Boolean = false
)

internal data class LandmarkTextSpec(
    val name: String,
    val shortLabel: String,
    val subtitle: String,
    val description: String,
    val ritualTip: String
)

object OfflineMapCatalog {

    internal val geoSpecs: List<LandmarkGeoSpec> = listOf(
        // --- HARAM PRECINCT ---
        LandmarkGeoSpec("h_haram", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.SANCTUARY, 21.4225, 39.8262, R.drawable.img_kaaba_haram, Scene3DType.KAABA, isPrimaryHub = true),
        LandmarkGeoSpec("h_safa", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.RITUAL, 21.4216, 39.8274, R.drawable.img_safa_marwa, Scene3DType.MASAA),
        LandmarkGeoSpec("h_marwah", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.RITUAL, 21.4248, 39.8271, R.drawable.img_safa_marwa, Scene3DType.MASAA),
        LandmarkGeoSpec("h_maqam", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.RITUAL, 21.4229, 39.8265, R.drawable.img_kaaba_haram, Scene3DType.KAABA),
        LandmarkGeoSpec("h_king_fahd", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.TRANSIT, 21.4217, 39.8238, R.drawable.img_kaaba_haram),
        LandmarkGeoSpec("h_abdulaziz", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.TRANSIT, 21.4201, 39.8258, R.drawable.img_kaaba_haram),
        LandmarkGeoSpec("h_carts", OfflineMapRegion.HARAM_PRECINCT, LandmarkCategory.TRANSIT, 21.4241, 39.8286, R.drawable.img_safa_marwa),

        // --- MAKKAH & MASHAAER ---
        LandmarkGeoSpec("m_haram", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.SANCTUARY, 21.4225, 39.8262, R.drawable.img_kaaba_haram, Scene3DType.KAABA, isPrimaryHub = true),
        LandmarkGeoSpec("m_jamarat", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.RITUAL, 21.4209, 39.8736, R.drawable.img_mina_jamarat, Scene3DType.JAMARAT),
        LandmarkGeoSpec("m_khaif", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.SANCTUARY, 21.4153, 39.8825, R.drawable.img_mina_jamarat, Scene3DType.JAMARAT),
        LandmarkGeoSpec("m_mina", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.RITUAL, 21.4133, 39.8980, R.drawable.img_mina_jamarat, Scene3DType.JAMARAT),
        LandmarkGeoSpec("m_muzdalifah", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.RITUAL, 21.3891, 39.9147, R.drawable.img_arafat_mount),
        LandmarkGeoSpec("m_namirah", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.SANCTUARY, 21.3526, 39.9665, R.drawable.img_arafat_mount, Scene3DType.ARAFAT),
        LandmarkGeoSpec("m_arafat", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.RITUAL, 21.3549, 39.9841, R.drawable.img_arafat_mount, Scene3DType.ARAFAT),
        LandmarkGeoSpec("m_taneem", OfflineMapRegion.MAKKAH_MASHAAER, LandmarkCategory.MIQAT, 21.4671, 39.8007, R.drawable.img_kaaba_haram),

        // --- MADINAH SANCTUARY ---
        LandmarkGeoSpec("d_nabawi", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.SANCTUARY, 24.4672, 39.6112, R.drawable.img_madinah_nabawi, Scene3DType.MADINAH, isPrimaryHub = true),
        LandmarkGeoSpec("d_rawdah", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.RITUAL, 24.4696, 39.6135, R.drawable.img_madinah_nabawi, Scene3DType.MADINAH),
        LandmarkGeoSpec("d_baqi", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.SANCTUARY, 24.4666, 39.6210, R.drawable.img_madinah_nabawi, Scene3DType.MADINAH),
        LandmarkGeoSpec("d_quba", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.SANCTUARY, 24.4393, 39.6173, R.drawable.img_madinah_nabawi),
        LandmarkGeoSpec("d_uhud", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.RITUAL, 24.5028, 39.6126, R.drawable.img_arafat_mount),
        LandmarkGeoSpec("d_hulayfah", OfflineMapRegion.MADINAH_SANCTUARY, LandmarkCategory.MIQAT, 24.4137, 39.5428, R.drawable.img_madinah_nabawi)
    )

    val routes: List<OfflineMapRoute> = listOf(
        // Mas'a corridor in Haram Precinct
        OfflineMapRoute(
            id = "r_masa",
            region = OfflineMapRegion.HARAM_PRECINCT,
            waypoints = listOf(21.4216 to 39.8274, 21.4248 to 39.8271),
            isRailway = false
        ),
        // Haram King Abdulaziz / King Fahd approach paths
        OfflineMapRoute(
            id = "r_haram_approach",
            region = OfflineMapRegion.HARAM_PRECINCT,
            waypoints = listOf(21.4217 to 39.8238, 21.4225 to 39.8262, 21.4201 to 39.8258),
            isRailway = false
        ),
        // Mashaaer Train railway (Mina -> Muzdalifah -> Arafat)
        OfflineMapRoute(
            id = "r_mashaaer_train",
            region = OfflineMapRegion.MAKKAH_MASHAAER,
            waypoints = listOf(21.4209 to 39.8736, 21.4133 to 39.8980, 21.3891 to 39.9147, 21.3549 to 39.9841),
            isRailway = true
        ),
        // Haram -> Mina pedestrian corridor
        OfflineMapRoute(
            id = "r_haram_mina_walk",
            region = OfflineMapRegion.MAKKAH_MASHAAER,
            waypoints = listOf(21.4225 to 39.8262, 21.4209 to 39.8736),
            isRailway = false
        ),
        // Madinah Quba pedestrian avenue
        OfflineMapRoute(
            id = "r_quba_walk",
            region = OfflineMapRegion.MADINAH_SANCTUARY,
            waypoints = listOf(24.4672 to 39.6112, 24.4393 to 39.6173),
            isRailway = false
        )
    )

    fun stringsFor(lang: AppLanguage): OfflineMapStrings = when (lang) {
        AppLanguage.AR -> arStrings
        AppLanguage.EN -> enStrings
        AppLanguage.FR -> frStrings
        AppLanguage.UR, AppLanguage.ID, AppLanguage.TR -> OfflineMapCatalogExtra.stringsFor(lang)
    }

    fun landmarksFor(lang: AppLanguage, region: OfflineMapRegion? = null): List<SacredLandmark> {
        val textMap = when (lang) {
            AppLanguage.AR -> arLandmarks
            AppLanguage.EN -> enLandmarks
            AppLanguage.FR -> frLandmarks
            AppLanguage.UR, AppLanguage.ID, AppLanguage.TR -> OfflineMapCatalogExtra.textMapFor(lang)
        }
        return geoSpecs
            .filter { region == null || it.region == region }
            .map { spec ->
                val txt = textMap[spec.id] ?: enLandmarks.getValue(spec.id)
                SacredLandmark(
                    id = spec.id,
                    region = spec.region,
                    category = spec.category,
                    lat = spec.lat,
                    lng = spec.lng,
                    name = txt.name,
                    shortLabel = txt.shortLabel,
                    subtitle = txt.subtitle,
                    description = txt.description,
                    ritualTip = txt.ritualTip,
                    photoRes = spec.photoRes,
                    scene3D = spec.scene3D,
                    isPrimaryHub = spec.isPrimaryHub
                )
            }
    }

    private val arStrings = OfflineMapStrings(
        cardTitle = "خريطة المعالم المقدسة (بدون إنترنت)",
        cardSubtitle = "تعمل بكامل دقتها دون اتصال بالإنترنت • الحرم والمشاعر والمدينة",
        offlineReadyBadge = "متاح أوفلاين 100%",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 المسجد الحرام",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ المشاعر المقدسة",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 المدينة المنورة"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "الكل",
            LandmarkCategory.SANCTUARY to "الحرم والمساجد",
            LandmarkCategory.RITUAL to "مواقع المناسك",
            LandmarkCategory.TRANSIT to "البوابات والخدمات",
            LandmarkCategory.MIQAT to "المواقيت"
        ),
        resetZoomHint = "إعادة الضبط",
        panZoomTip = "اسحب للتحريك أو كبّر بالإصبعين واضغط أي معلم",
        distFromHaramLabel = "المسافة عن المركز",
        distFromYouLabel = "المسافة عن موقعك",
        coordsLabel = "الإحداثيات",
        tipPrefix = "إرشاد نسكي:",
        open3DBtn = "عرض 3D",
        listenBtn = "استمع",
        railwayLegend = "قطار المشاعر",
        walkPathLegend = "مسار المشاة",
        youMarkerLabel = "موقعك",
        hotelMarkerLabel = "فندقك",
        kmUnit = "كم",
        mUnit = "م"
    )

    private val enStrings = OfflineMapStrings(
        cardTitle = "Offline Sacred Landmarks Map",
        cardSubtitle = "Works 100% offline with built-in coordinates • Haram, Mashaaer & Madinah",
        offlineReadyBadge = "Offline Ready",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 Al-Masjid al-Haram",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ Mashaaer Route",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 Madinah Sanctuary"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "All",
            LandmarkCategory.SANCTUARY to "Sanctuaries",
            LandmarkCategory.RITUAL to "Ritual Sites",
            LandmarkCategory.TRANSIT to "Gates & Access",
            LandmarkCategory.MIQAT to "Miqats"
        ),
        resetZoomHint = "Reset View",
        panZoomTip = "Drag to pan, pinch to zoom, and tap any landmark pin",
        distFromHaramLabel = "From Central Hub",
        distFromYouLabel = "From Your Location",
        coordsLabel = "Coordinates",
        tipPrefix = "Pilgrim Tip:",
        open3DBtn = "View 3D",
        listenBtn = "Listen",
        railwayLegend = "Mashaaer Train",
        walkPathLegend = "Walking Corridor",
        youMarkerLabel = "You",
        hotelMarkerLabel = "Hotel",
        kmUnit = "km",
        mUnit = "m"
    )

    private val frStrings = OfflineMapStrings(
        cardTitle = "Carte Hors Ligne des Lieux Saints",
        cardSubtitle = "100% accessible sans internet • Haram, Mashaaer et Médine",
        offlineReadyBadge = "Hors Ligne",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 Al-Masjid al-Haram",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ Sites du Hajj",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 Médine"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "Tout",
            LandmarkCategory.SANCTUARY to "Sanctuaires",
            LandmarkCategory.RITUAL to "Rites",
            LandmarkCategory.TRANSIT to "Portes & Accès",
            LandmarkCategory.MIQAT to "Miqats"
        ),
        resetZoomHint = "Réinitialiser",
        panZoomTip = "Glissez, pincez pour zoomer et touchez un repère",
        distFromHaramLabel = "Depuis le Centre",
        distFromYouLabel = "Votre Distance",
        coordsLabel = "Coordonnées",
        tipPrefix = "Conseil:",
        open3DBtn = "Vue 3D",
        listenBtn = "Écouter",
        railwayLegend = "Train Mashaaer",
        walkPathLegend = "Voie Piétonne",
        youMarkerLabel = "Vous",
        hotelMarkerLabel = "Hôtel",
        kmUnit = "km",
        mUnit = "m"
    )

    private val arLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("المسجد الحرام والكعبة المشرفة", "الكعبة المشرفة", "قلب المطاف والقبلة", "أقدس بقاع الأرض؛ يضم الكعبة المشرفة والحجر الأسود وصحن المطاف.", "ابدأ الطواف بمحاذاتك للحجر الأسود واجعل الكعبة عن يسارك."),
        "h_safa" to LandmarkTextSpec("جبل الصفا (بداية السعي)", "الصفا", "نقطة انطلاق السعي", "يقع في الجهة الجنوبية من المسعى؛ منه يبدأ المعتمر والحاج أشواط السعي السبعة.", "استقبل الكعبة عند الوقوف على الصفا واذكر الله وادعُ."),
        "h_marwah" to LandmarkTextSpec("جبل المروة (نهاية السعي)", "المروة", "ختام الأشواط الزوجية", "يقع في الطرف الشمالي من المسعى؛ ينتهي عنده الشوط السابع من السعي.", "الأشواط 1،3،5،7 تنتهي هنا، وبعد السابع يتم التقصير أو الحلق."),
        "h_maqam" to LandmarkTextSpec("مقام إبراهيم وبئر زمزم", "مقام إبراهيم", "مصلى سنة الطواف", "الحجر الذي قام عليه نبي الله إبراهيم عليه السلام، وخلفه تُصلّى ركعتا الطواف.", "إن ازدحم المطاف فتجوز صلاة الركعتين في أي مكان داخل المسجد الحرام."),
        "h_king_fahd" to LandmarkTextSpec("باب الملك فهد (توسعة الحرم)", "باب الملك فهد", "مدخل رئيسي غربي", "من أكبر البوابات الغربية المؤدية مباشرة إلى صحن المطاف والأدوار العلوية.", "اجعل رقم البوابة علامة مرجعية سهلة لالتقاء مجموعتك."),
        "h_abdulaziz" to LandmarkTextSpec("باب الملك عبدالعزيز وساحة الساعة", "باب الملك عبدالعزيز", "مدخل جنوبي رئيسي", "البوابة الرئيسية المواجهة لوقف الملك عبدالعزيز وأبراج الساعة ومحطة الحافلات.", "مخرج ميسّر للوصول إلى الفنادق الجنوبية ودورات المياه."),
        "h_carts" to LandmarkTextSpec("منصة العربات الكهربائية (المروة)", "العربات", "دور العربات والميزانين", "موقع استئجار العربات الكهربائية لكبار السن وذوي الاحتياجات لأداء الطواف والسعي.", "استخدم مصاعد المروة أو أجياد للوصول المباشر لطابق العربات."),

        "m_haram" to LandmarkTextSpec("المسجد الحرام (مكة المكرمة)", "المسجد الحرام", "نقطة الانطلاق والعودة", "المسجد الذي يُقصد لطواف القدوم والإفاضة والوداع وطواف العمرة.", "احتفظ ببطاقتك الذكية وموقع فندقك محفوظين أوفلاين قبل الخروج."),
        "m_jamarat" to LandmarkTextSpec("منشأة الجمرات الثلاث", "الجمرات", "الصغرى • الوسطى • الكبرى", "جسر الجمرات متعدد الطوابق بمنى لرمي جمرة العقبة وأيام التشريق.", "التزم بمسارات التفويج المحددة وتجنب وقت اشتداد الزحام."),
        "m_khaif" to LandmarkTextSpec("مسجد الخيف بمشعر منى", "مسجد الخيف", "المسجد التاريخي بمنى", "يقع في سفح جبل منى الجنوبي بالقرب من الجمرات وصلى فيه الأنبياء.", "معلم بارز لتحديد الاتجاهات وسط مخيمات منى."),
        "m_mina" to LandmarkTextSpec("مشعر منى (مدينة الخيام)", "مشعر منى", "مبيت التروية والتشريق", "وادي الخيام البيضاء على بُعد ~5 كم شرق الحرم حيث يبيت الحجاج يوم 8 وأيام التشريق.", "تعرّف على رقم المربع والشارع لمخيم حملتك فور الوصول."),
        "m_muzdalifah" to LandmarkTextSpec("مشعر مزدلفة (المشعر الحرام)", "مزدلفة", "المبيت ليلة العاشر", "المشعر الواقع بين منى وعرفات؛ يُجمع فيه المغرب والعشاء ويُبات فيه وتُلقط الحصى.", "احمل حصيرة خفيفة واجمع 7 حصيات بحجم حبة الحمص لجمرة العقبة."),
        "m_namirah" to LandmarkTextSpec("مسجد نمرة بعرفات", "مسجد نمرة", "خطبة يوم عرفة", "المسجد الكبير غرب عرفات؛ تُلقى فيه خطبة عرفة ويُجمع فيه الظهر والعصر قصراً.", "تأكد من الوقوف داخل حدود مشعر عرفات وليس خارجها."),
        "m_arafat" to LandmarkTextSpec("مشعر عرفات وجبل الرحمة", "جبل الرحمة", "ركن الحج الأعظم", "سهل عرفات وجبل الرحمة على بُعد ~20 كم من مكة حيث يجتمع الحجاج يوم 9 ذي الحجة.", "استغل ساعات بعد الظهر وحتى الغروب في الدعاء الخالص والذكر."),
        "m_taneem" to LandmarkTextSpec("مسجد السيدة عائشة (التنعيم)", "التنعيم", "أقرب ميقات لأهل مكة", "يقع شمال مكة على طريق المدينة ويحرم منه من كان داخل مكة وأراد عمرة إضافية.", "تتوفر فيه مرافق الوضوء ولبس الإحرام على مدار الساعة."),

        "d_nabawi" to LandmarkTextSpec("المسجد النبوي الشريف", "المسجد النبوي", "الحرم المدني والقبة الخضراء", "مسجد رسول الله ﷺ؛ الصلاة فيه خير من ألف صلاة فيما سواه إلا المسجد الحرام.", "ادخل باليمنى وادعُ بدعاء دخول المسجد وحافظ على السكينة."),
        "d_rawdah" to LandmarkTextSpec("الروضة الشريفة", "الروضة الشريفة", "روضة من رياض الجنة", "المساحة المباركة بين بيت النبي ﷺ ومنبره داخل المسجد النبوي.", "تأكد من إظهار تصريح نسك عند البوابة المخصصة لدخول الروضة."),
        "d_baqi" to LandmarkTextSpec("بقيع الغرقد", "البقيع", "مقبرة الصحابة رضوان الله عليهم", "المقبرة التاريخية شرق المسجد النبوي وفيها آلاف من الصحابة وآل البيت.", "تُزار من الممشى الخارجي المفتوح بعد الفجر والعصر للسلام والدعاء."),
        "d_quba" to LandmarkTextSpec("مسجد قباء", "مسجد قباء", "أول مسجد بُني في الإسلام", "يقع جنوب المسجد النبوي ويربطهما ممشى جادة قباء؛ الصلاة فيه كأجر عمرة.", "توضأ في سكنك ثم سر أو اركب إلى قباء وصلِّ ركعتين."),
        "d_uhud" to LandmarkTextSpec("جبل أحد ومقبرة الشهداء", "جبل أحد", "جبل يحبنا ونحبه", "الجبل التاريخي شمال المدينة وبسفحه ساحة غزوة أحد وشهدائها.", "يُسلم على شهداء أحد ويدعى لهم بالمغفرة والرضوان."),
        "d_hulayfah" to LandmarkTextSpec("ميقات ذي الحليفة (آبار علي)", "ذو الحليفة", "ميقات أهل المدينة والقادمين منها", "أبعد المواقيت عن مكة (~430 كم) ومنه يعقد المحرمون نية العمرة أو الحج.", "يمكن ارتداء الإحرام في الفندق ثم عقد النية فور الوصول للميقات.")
    )

    private val enLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("Al-Masjid al-Haram & Holy Kaaba", "Holy Kaaba", "Center of Mataf & Qibla", "The holiest sanctuary on earth housing the Kaaba, the Black Stone, and the Mataf courtyard.", "Begin Tawaf aligned with the Black Stone while keeping the Kaaba to your left."),
        "h_safa" to LandmarkTextSpec("Mount Safa (Sa'i Start)", "Safa", "Starting point of Sa'i", "Located on the southern end of the Mas'a corridor where all 7 Sa'i laps begin.", "Face the Kaaba atop Safa, praise Allah, and make sincere supplications."),
        "h_marwah" to LandmarkTextSpec("Mount Marwah (Sa'i End)", "Marwah", "Final stop of Lap 7", "Northern end of the Mas'a corridor where odd laps (1, 3, 5, 7) conclude.", "Upon completing Lap 7 here, proceed to shaving or trimming hair (Tahallul)."),
        "h_maqam" to LandmarkTextSpec("Maqam Ibrahim & Zamzam Area", "Maqam Ibrahim", "Station of Prophet Ibrahim", "Stands near the Kaaba door; pray two Sunnah Rak'ahs behind it after finishing Tawaf.", "If Mataf is crowded, praying the two Rak'ahs anywhere in the mosque is valid."),
        "h_king_fahd" to LandmarkTextSpec("King Fahd Gate", "King Fahd Gate", "Major Western Entrance", "Main western expansion gate giving direct access to ground & upper Mataf decks.", "Use gate numbers as unmistakable offline meeting points for your group."),
        "h_abdulaziz" to LandmarkTextSpec("King Abdulaziz Gate & Clock Plaza", "Abdulaziz Gate", "Southern Sanctuary Entrance", "Primary southern entrance facing Abraj Al-Bait Clock Tower and bus terminals.", "Convenient exit toward southern hotels, medical posts, and shuttle stops."),
        "h_carts" to LandmarkTextSpec("Electric Carts Terrace (Marwah)", "Carts Terrace", "Accessible Mobility Deck", "Pickup zone for electric scooters and wheelchairs serving Tawaf and Sa'i levels.", "Take the Marwah or Ajyad elevators to reach the mobility terrace directly."),

        "m_haram" to LandmarkTextSpec("Al-Masjid al-Haram (Makkah)", "Haram Mosque", "Origin & Return Sanctuary", "Central sanctuary for Umrah, Tawaf al-Qudum, Tawaf al-Ifadah, and Farewell Tawaf.", "Save your hotel GPS pin offline before heading out to the Mashaaer sites."),
        "m_jamarat" to LandmarkTextSpec("Jamarat Stoning Bridge", "Jamarat", "Sughra • Wusta • Kubra", "Multi-level bridge in western Mina housing the three Jamarat pillars.", "Follow scheduled crowd-flow corridors and stone calmly in sequence."),
        "m_khaif" to LandmarkTextSpec("Masjid al-Khaif (Mina)", "Al-Khaif", "Historic Mosque of Mina", "Large historic mosque on the southern slope of Mina near the Jamarat complex.", "Serves as a clear orientation landmark across the Mina tent valley."),
        "m_mina" to LandmarkTextSpec("Mina Tent City", "Mina Tents", "8th Dhul-Hijjah & Tashreeq", "Valley of fireproof tents ~5 km east of Al-Haram where pilgrims stay during Hajj.", "Note your tent camp sector and street number immediately upon check-in."),
        "m_muzdalifah" to LandmarkTextSpec("Muzdalifah (Al-Mash'ar al-Haram)", "Muzdalifah", "Night Stay Under Open Sky", "Open plain between Arafat and Mina for combining Maghrib/Isha and gathering pebbles.", "Collect 7 chickpea-sized pebbles for Jamarat al-Aqabah on the 10th."),
        "m_namirah" to LandmarkTextSpec("Masjid Namirah (Arafat)", "Masjid Namirah", "Arafat Sermon Mosque", "Iconic mosque on the western border of Arafat hosting the Day of Arafah Khutbah.", "Ensure you remain inside the official Arafat boundary signs until sunset."),
        "m_arafat" to LandmarkTextSpec("Mount Arafat (Jabal al-Rahmah)", "Arafat", "Supreme Pillar of Hajj", "Plain and hill ~20 km east of Makkah where pilgrims stand in dua on 9th Dhul-Hijjah.", "Dedicate the hours from Dhuhr until sunset to heartfelt dua and istighfar."),
        "m_taneem" to LandmarkTextSpec("Masjid Aisha (At-Tan'eem)", "At-Tan'eem", "Nearest Miqat to Makkah", "Northern Makkah boundary mosque (~7.5 km from Haram) for entering Ihram.", "Open 24/7 with ablution halls and Ihram changing facilities."),

        "d_nabawi" to LandmarkTextSpec("Al-Masjid an-Nabawi", "Prophet's Mosque", "Green Dome & Sanctuary", "The Prophet's Mosque in Madinah; a prayer here equals 1,000 prayers elsewhere.", "Enter peacefully with your right foot and send abundant salutations upon the Prophet."),
        "d_rawdah" to LandmarkTextSpec("Ar-Rawdah Ash-Sharifah", "Ar-Rawdah", "Garden of Paradise", "Sacred area between the Prophet's pulpit and Sacred Chamber inside Al-Masjid an-Nabawi.", "Have your Nusuk permit ready at the designated Rawdah courtyard gate."),
        "d_baqi" to LandmarkTextSpec("Jannat al-Baqi Cemetery", "Al-Baqi", "Resting Place of Companions", "Historic cemetery east of Al-Masjid an-Nabawi where thousands of Companions rest.", "Visited from the outer perimeter gates after Fajr and Asr to offer salam & dua."),
        "d_quba" to LandmarkTextSpec("Masjid Quba", "Masjid Quba", "First Mosque in Islam", "Located ~3.5 km south via the shaded Quba pedestrian avenue; 2 Rak'ahs equal an Umrah reward.", "Perform Wudu at your hotel before walking or riding to Quba."),
        "d_uhud" to LandmarkTextSpec("Mount Uhud & Martyrs Site", "Mount Uhud", "Historic Northern Ridge", "Historic mountain north of Madinah overlooking the Battle of Uhud site.", "Greet the martyrs of Uhud and make dua for them."),
        "d_hulayfah" to LandmarkTextSpec("Dhul Hulayfah Miqat (Abyar Ali)", "Dhul Hulayfah", "Miqat for Madinah Travelers", "Primary Miqat ~430 km from Makkah for pilgrims departing Madinah toward Makkah.", "Wear Ihram garments before departure and pronounce Niyyah at the Miqat.")
    )

    private val frLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("Al-Masjid al-Haram & Kaaba", "Sainte Kaaba", "Centre du Mataf & Qibla", "Le lieu le plus sacré de l'Islam abritant la Kaaba, la Pierre Noire et le Mataf.", "Débutez le Tawaf face à la Pierre Noire en gardant la Kaaba à votre gauche."),
        "h_safa" to LandmarkTextSpec("Mont Safa (Départ du Sa'i)", "Safa", "Départ du Sa'i", "Extrémité sud du couloir du Mas'a où commencent les 7 trajets du Sa'i.", "Faites face à la Kaaba sur Safa et multipliez les invocations."),
        "h_marwah" to LandmarkTextSpec("Mont Marwah (Fin du Sa'i)", "Marwah", "Fin du 7e trajet", "Extrémité nord du Mas'a où s'achèvent les trajets impairs et le 7e tour.", "Après le 7e trajet ici, procédez au rasage ou à la coupe (Tahallul)."),
        "h_maqam" to LandmarkTextSpec("Maqam Ibrahim & Zamzam", "Maqam Ibrahim", "Station d'Ibrahim", "Lieu de prière des 2 Rak'ahs de Sunnah après les 7 tours de Tawaf.", "En cas d'affluence, priez les 2 Rak'ahs n'importe où dans la mosquée."),
        "h_king_fahd" to LandmarkTextSpec("Porte Roi Fahd", "Porte Roi Fahd", "Entrée Ouest Principale", "Grande porte ouest donnant accès direct au parvis du Mataf et aux étages.", "Notez le numéro de porte comme point de rendez-vous hors ligne."),
        "h_abdulaziz" to LandmarkTextSpec("Porte Roi Abdelaziz", "Porte Abdelaziz", "Entrée Sud & Tour Horloge", "Entrée sud face aux tours Abraj Al-Bait et aux terminaux de bus.", "Sortie idéale vers les hôtels du sud et les postes médicaux."),
        "h_carts" to LandmarkTextSpec("Terrasse Chariots (Marwah)", "Chariots", "Accès Mobilité Réduite", "Zone de fauteuils roulants et chariots électriques pour le Tawaf et le Sa'i.", "Utilisez les ascenseurs côté Marwah ou Ajyad."),

        "m_haram" to LandmarkTextSpec("Al-Masjid al-Haram", "Masjid al-Haram", "Sanctuaire Central", "Mosquée Sacrée pour le Tawaf de la Omra, d'Ifadah et d'adieu.", "Enregistrez la position GPS de votre hôtel hors ligne avant le départ."),
        "m_jamarat" to LandmarkTextSpec("Pont des Jamarat", "Jamarat", "Sughra • Wusta • Kubra", "Complexe à étages de Mina pour le jet des pierres durant le Hajj.", "Suivez les couloirs assignés et lancez les cailloux avec sérénité."),
        "m_khaif" to LandmarkTextSpec("Mosquée al-Khaif (Mina)", "Al-Khaif", "Mosquée Historique", "Grande mosquée historique au pied sud de Mina près des Jamarat.", "Point de repère majeur dans la vallée des tentes de Mina."),
        "m_mina" to LandmarkTextSpec("Vallée de Mina (Tentes)", "Mina", "8 Dhoul-Hijja & Tachriq", "Cité des tentes à ~5 km à l'est du Haram où résident les pèlerins.", "Mémorisez le numéro de bloc et de rue de votre campement."),
        "m_muzdalifah" to LandmarkTextSpec("Muzdalifah", "Muzdalifah", "Nuit à la belle étoile", "Plaine entre Arafat et Mina pour prier Maghrib/Isha et ramasser les cailloux.", "Collectez 7 petits cailloux pour la Grande Jamrah du 10e jour."),
        "m_namirah" to LandmarkTextSpec("Mosquée Namirah (Arafat)", "Namirah", "Mosquée du Sermon", "Grande mosquée à l'ouest d'Arafat accueillant le sermon du jour d'Arafat.", "Vérifiez que vous vous trouvez bien à l'intérieur des limites d'Arafat."),
        "m_arafat" to LandmarkTextSpec("Mont Arafat (Jabal al-Rahmah)", "Arafat", "Pilier Suprême du Hajj", "Plaine et colline à ~20 km de La Mecque où les pèlerins invoquent le 9e jour.", "Consacrez l'après-midi jusqu'au coucher du soleil aux invocations."),
        "m_taneem" to LandmarkTextSpec("Mosquée Aïcha (Tan'eem)", "At-Tan'eem", "Miqat le plus proche", "Miqat au nord de La Mecque pour entrer en état d'Ihram.", "Installations d'ablutions ouvertes 24h/24."),

        "d_nabawi" to LandmarkTextSpec("Mosquée du Prophète", "Masjid Nabawi", "Dôme Vert & Sanctuaire", "À Médine ; une prière y vaut mille prières ailleurs (hors La Mecque).", "Entrez du pied droit avec humilité et multipliez les salutations."),
        "d_rawdah" to LandmarkTextSpec("Ar-Rawdah Ash-Sharifah", "Ar-Rawdah", "Jardin du Paradis", "Espace béni entre la chaire prophétique et la Chambre Sacrée.", "Préparez votre permis Nusuk à la porte dédiée."),
        "d_baqi" to LandmarkTextSpec("Cimetière d'Al-Baqi", "Al-Baqi", "Compagnons du Prophète", "Cimetière historique à l'est de la Mosquée du Prophète.", "Visite depuis l'esplanade extérieure après Fajr et Asr."),
        "d_quba" to LandmarkTextSpec("Mosquée de Quba", "Mosquée Quba", "Première Mosquée", "Reliée au Haram par l'allée piétonne de Quba ; 2 Rak'ahs valent une Omra.", "Faites vos ablutions avant le départ pour Quba."),
        "d_uhud" to LandmarkTextSpec("Mont Uhud", "Mont Uhud", "Site des Martyrs", "Montagne historique au nord de Médine surplombant la plaine d'Uhud.", "Saluez les martyrs d'Uhud et priez pour eux."),
        "d_hulayfah" to LandmarkTextSpec("Miqat Dhul Hulayfah", "Dhul Hulayfah", "Miqat de Médine", "Miqat principal des voyageurs partant de Médine vers La Mecque (~430 km).", "Revêtez l'Ihram avant le départ et formulez la Niyyah ici.")
    )
}
