package com.example.model

import androidx.annotation.DrawableRes

enum class OfflineMapRegion(
    val id: String,
    val minLat: Double,
    val maxLat: Double,
    val minLng: Double,
    val maxLng: Double,
    val scaleLabelMeters: Int
) {
    HARAM_PRECINCT(
        id = "haram",
        minLat = 21.4192,
        maxLat = 21.4258,
        minLng = 39.8220,
        maxLng = 39.8300,
        scaleLabelMeters = 200
    ),
    MAKKAH_MASHAAER(
        id = "mashaaer",
        minLat = 21.3420,
        maxLat = 21.4760,
        minLng = 39.7900,
        maxLng = 40.0020,
        scaleLabelMeters = 5000
    ),
    MADINAH_SANCTUARY(
        id = "madinah",
        minLat = 24.4050,
        maxLat = 24.5100,
        minLng = 39.5350,
        maxLng = 39.6750,
        scaleLabelMeters = 3000
    )
}

enum class LandmarkCategory {
    ALL,
    SANCTUARY,
    RITUAL,
    TRANSIT,
    MIQAT
}

data class SacredLandmark(
    val id: String,
    val region: OfflineMapRegion,
    val category: LandmarkCategory,
    val lat: Double,
    val lng: Double,
    val name: String,
    val shortLabel: String,
    val subtitle: String,
    val description: String,
    val ritualTip: String,
    @DrawableRes val photoRes: Int,
    val scene3D: Scene3DType? = null,
    val isPrimaryHub: Boolean = false
)

data class OfflineMapRoute(
    val id: String,
    val region: OfflineMapRegion,
    val waypoints: List<Pair<Double, Double>>,
    val isRailway: Boolean
)

data class OfflineMapStrings(
    val cardTitle: String,
    val cardSubtitle: String,
    val offlineReadyBadge: String,
    val regionLabels: Map<OfflineMapRegion, String>,
    val categoryLabels: Map<LandmarkCategory, String>,
    val resetZoomHint: String,
    val panZoomTip: String,
    val distFromHaramLabel: String,
    val distFromYouLabel: String,
    val coordsLabel: String,
    val tipPrefix: String,
    val open3DBtn: String,
    val listenBtn: String,
    val railwayLegend: String,
    val walkPathLegend: String,
    val youMarkerLabel: String,
    val hotelMarkerLabel: String,
    val kmUnit: String,
    val mUnit: String
)
