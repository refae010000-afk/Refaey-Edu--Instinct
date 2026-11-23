package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.OfflineMapCatalog
import com.example.data.OfflineMapMath
import com.example.model.AppLanguage
import com.example.model.LandmarkCategory
import com.example.model.OfflineMapRegion
import com.example.model.OfflineMapRoute
import com.example.model.OfflineMapStrings
import com.example.model.SacredLandmark
import com.example.model.Scene3DType
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.util.GeoMath
import java.util.Locale
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun OfflineSacredMapCard(
    language: AppLanguage,
    darkMode: Boolean,
    userLat: Double?,
    userLng: Double?,
    hotelLat: Double?,
    hotelLng: Double?,
    onJumpTo3D: (Scene3DType) -> Unit,
    onSpeak: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapStrings = remember(language) { OfflineMapCatalog.stringsFor(language) }
    var selectedRegion by remember { mutableStateOf(OfflineMapRegion.HARAM_PRECINCT) }
    var selectedCategory by remember { mutableStateOf(LandmarkCategory.ALL) }

    val regionLandmarks = remember(language, selectedRegion) {
        OfflineMapCatalog.landmarksFor(language, selectedRegion)
    }
    val visibleLandmarks = remember(regionLandmarks, selectedCategory) {
        if (selectedCategory == LandmarkCategory.ALL) regionLandmarks
        else regionLandmarks.filter { it.category == selectedCategory }
    }
    val regionRoutes = remember(selectedRegion) {
        OfflineMapCatalog.routes.filter { it.region == selectedRegion }
    }

    var selectedLandmarkId by remember { mutableStateOf("h_haram") }
    LaunchedEffect(selectedRegion, visibleLandmarks) {
        if (visibleLandmarks.none { it.id == selectedLandmarkId }) {
            selectedLandmarkId = visibleLandmarks.firstOrNull { it.isPrimaryHub }?.id
                ?: visibleLandmarks.firstOrNull()?.id.orEmpty()
        }
    }
    val selectedLandmark = remember(visibleLandmarks, selectedLandmarkId) {
        visibleLandmarks.find { it.id == selectedLandmarkId } ?: visibleLandmarks.firstOrNull()
    }

    var zoom by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(selectedRegion) {
        zoom = 1f
        panOffset = Offset.Zero
        selectedCategory = LandmarkCategory.ALL
    }

    PilgrimCard(modifier = modifier, accentStartBar = true) {
        // Header + Offline Ready Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(
                title = mapStrings.cardTitle,
                subtitle = mapStrings.cardSubtitle,
                icon = Icons.Outlined.Map,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = StatusSuccessContainer,
                border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.55f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WifiOff,
                        contentDescription = mapStrings.offlineReadyBadge,
                        tint = StatusSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = mapStrings.offlineReadyBadge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = StatusSuccess
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Region Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OfflineMapRegion.entries.forEach { region ->
                PilgrimChip(
                    label = mapStrings.regionLabels[region] ?: region.id,
                    selected = selectedRegion == region,
                    onClick = { selectedRegion = region },
                    tag = "offline_map_region_${region.id}"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Row
        val availableCats = remember(regionLandmarks) {
            listOf(LandmarkCategory.ALL) + LandmarkCategory.entries.filter { cat ->
                cat != LandmarkCategory.ALL && regionLandmarks.any { it.category == cat }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            availableCats.forEach { cat ->
                PilgrimChip(
                    label = mapStrings.categoryLabels[cat] ?: cat.name,
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    tag = "offline_map_cat_${cat.name.lowercase()}"
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Offline Vector Map Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(292.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, SacredGold.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
        ) {
            OfflineVectorMapCanvas(
                region = selectedRegion,
                landmarks = visibleLandmarks,
                routes = regionRoutes,
                selectedLandmarkId = selectedLandmark?.id,
                strings = mapStrings,
                darkMode = darkMode || isSystemInDarkTheme(),
                userLat = userLat,
                userLng = userLng,
                hotelLat = hotelLat,
                hotelLng = hotelLng,
                zoom = zoom,
                panOffset = panOffset,
                onTransform = { zDelta, panDelta ->
                    val nextZoom = (zoom * zDelta).coerceIn(1f, 3.2f)
                    zoom = nextZoom
                    val maxPan = 220f * (nextZoom - 0.8f)
                    panOffset = Offset(
                        x = (panOffset.x + panDelta.x).coerceIn(-maxPan, maxPan),
                        y = (panOffset.y + panDelta.y).coerceIn(-maxPan, maxPan)
                    )
                },
                onSelectLandmark = { landmark ->
                    selectedLandmarkId = landmark.id
                }
            )

            // Top-Start Offline Compass Badge & Tip
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color.Black.copy(alpha = 0.56f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Explore,
                        contentDescription = "N",
                        tint = SacredGoldBright,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "N ↑ • ${String.format(Locale.US, "%.1fx", zoom)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Top-End Zoom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MapControlIconButton(
                    icon = Icons.Outlined.Add,
                    contentDesc = "Zoom In",
                    tag = "map_zoom_in"
                ) {
                    zoom = (zoom + 0.45f).coerceAtMost(3.2f)
                }
                MapControlIconButton(
                    icon = Icons.Outlined.Remove,
                    contentDesc = "Zoom Out",
                    tag = "map_zoom_out"
                ) {
                    val next = (zoom - 0.45f).coerceAtLeast(1f)
                    zoom = next
                    if (next <= 1.05f) panOffset = Offset.Zero
                }
                MapControlIconButton(
                    icon = Icons.Outlined.Refresh,
                    contentDesc = mapStrings.resetZoomHint,
                    tag = "map_reset_view"
                ) {
                    zoom = 1f
                    panOffset = Offset.Zero
                }
            }

            // Bottom Scale & Route Legend Bar
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color.Black.copy(alpha = 0.62f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedRegion == OfflineMapRegion.HARAM_PRECINCT) {
                            "📏 ~${selectedRegion.scaleLabelMeters} ${mapStrings.mUnit}"
                        } else {
                            "📏 ~${selectedRegion.scaleLabelMeters / 1000} ${mapStrings.kmUnit}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SacredGoldBright
                    )
                    Text(
                        text = if (selectedRegion == OfflineMapRegion.MAKKAH_MASHAAER) {
                            "━ ${mapStrings.railwayLegend}  ┄ ${mapStrings.walkPathLegend}"
                        } else {
                            mapStrings.panZoomTip
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick-Jump Landmark Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            visibleLandmarks.forEach { lm ->
                PilgrimChip(
                    label = lm.shortLabel,
                    selected = selectedLandmark?.id == lm.id,
                    onClick = { selectedLandmarkId = lm.id },
                    tag = "landmark_chip_${lm.id}"
                )
            }
        }

        // Selected Landmark Offline Detail Panel
        if (selectedLandmark != null) {
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedContent(
                targetState = selectedLandmark,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "selected_landmark_detail"
            ) { lm ->
                OfflineLandmarkDetailCard(
                    landmark = lm,
                    language = language,
                    strings = mapStrings,
                    userLat = userLat,
                    userLng = userLng,
                    onJumpTo3D = onJumpTo3D,
                    onSpeak = onSpeak
                )
            }
        }
    }
}

@Composable
private fun MapControlIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.Black.copy(alpha = 0.62f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(40.dp)
                .minimumInteractiveComponentSize()
                .testTag(tag)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun OfflineVectorMapCanvas(
    region: OfflineMapRegion,
    landmarks: List<SacredLandmark>,
    routes: List<OfflineMapRoute>,
    selectedLandmarkId: String?,
    strings: OfflineMapStrings,
    darkMode: Boolean,
    userLat: Double?,
    userLng: Double?,
    hotelLat: Double?,
    hotelLng: Double?,
    zoom: Float,
    panOffset: Offset,
    onTransform: (Float, Offset) -> Unit,
    onSelectLandmark: (SacredLandmark) -> Unit
) {
    val bgTop = if (darkMode) Color(0xFF0A1B16) else Color(0xFFF6F1E4)
    val bgBottom = if (darkMode) Color(0xFF102A22) else Color(0xFFE7DFCB)
    val gridColor = if (darkMode) Color(0xFF285143) else Color(0xFFCBBFA5)
    val terrainColor = if (darkMode) Color(0xFF17382D) else Color(0xFFDDD2B9)
    val labelBg = if (darkMode) Color(0xCC061611) else Color(0xE6FFFFFF)
    val labelText = if (darkMode) Color.White else Color(0xFF162721)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .testTag("offline_map_canvas")
            .pointerInput(region, zoom, panOffset) {
                detectTransformGestures { _, pan, zoomChange, _ ->
                    onTransform(zoomChange, pan)
                }
            }
            .pointerInput(region, landmarks, zoom, panOffset) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val center = Offset(w / 2f, h / 2f)
                    var closest: SacredLandmark? = null
                    var bestDist = Float.MAX_VALUE
                    landmarks.forEach { lm ->
                        val (nx, ny) = OfflineMapMath.project(lm.lat, lm.lng, region)
                        val px = center.x + (nx * w - center.x) * zoom + panOffset.x
                        val py = center.y + (ny * h - center.y) * zoom + panOffset.y
                        val dist = hypot(tapOffset.x - px, tapOffset.y - py)
                        if (dist < bestDist && dist <= 72f) {
                            bestDist = dist
                            closest = lm
                        }
                    }
                    closest?.let(onSelectLandmark)
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        fun toScreen(nx: Float, ny: Float): Offset {
            return Offset(
                x = center.x + (nx * w - center.x) * zoom + panOffset.x,
                y = center.y + (ny * h - center.y) * zoom + panOffset.y
            )
        }

        fun toLatLngScreen(lat: Double, lng: Double): Offset {
            val (nx, ny) = OfflineMapMath.project(lat, lng, region)
            return toScreen(nx, ny)
        }

        // 1. Topographic Surface Gradient
        drawRect(brush = Brush.verticalGradient(listOf(bgTop, bgBottom)))

        // 2. Latitude/Longitude Coordinate Graticule Grid
        for (i in 1..4) {
            val f = i / 5f
            val x = toScreen(f, 0f).x
            val y = toScreen(0f, f).y
            drawLine(gridColor.copy(alpha = 0.45f), Offset(x, 0f), Offset(x, h), strokeWidth = 1.dp.toPx())
            drawLine(gridColor.copy(alpha = 0.45f), Offset(0f, y), Offset(w, y), strokeWidth = 1.dp.toPx())
        }

        // 3. Architectural Footprint (Haram Courtyard or Mountain Contours)
        if (region == OfflineMapRegion.HARAM_PRECINCT) {
            val haramCenter = toLatLngScreen(GeoMath.KAABA_LAT, GeoMath.KAABA_LNG)
            val rx = 68.dp.toPx() * zoom
            val ry = 54.dp.toPx() * zoom
            drawOval(
                color = terrainColor.copy(alpha = 0.7f),
                topLeft = Offset(haramCenter.x - rx, haramCenter.y - ry),
                size = Size(rx * 2, ry * 2)
            )
            drawOval(
                color = SacredGold.copy(alpha = 0.55f),
                topLeft = Offset(haramCenter.x - rx, haramCenter.y - ry),
                size = Size(rx * 2, ry * 2),
                style = Stroke(width = 2.dp.toPx())
            )
            val kaabaSide = 12.dp.toPx() * zoom.coerceAtMost(1.8f)
            drawRect(
                color = Color(0xFF121212),
                topLeft = Offset(haramCenter.x - kaabaSide / 2f, haramCenter.y - kaabaSide / 2f),
                size = Size(kaabaSide, kaabaSide)
            )
            drawRect(
                color = SacredGoldBright,
                topLeft = Offset(haramCenter.x - kaabaSide / 2f, haramCenter.y - kaabaSide / 2f),
                size = Size(kaabaSide, kaabaSide),
                style = Stroke(width = 1.5.dp.toPx())
            )
        } else {
            val c1 = toScreen(0.24f, 0.30f)
            val c2 = toScreen(0.76f, 0.70f)
            drawCircle(terrainColor.copy(alpha = 0.55f), 58.dp.toPx() * zoom, c1, style = Stroke(1.5.dp.toPx()))
            drawCircle(terrainColor.copy(alpha = 0.45f), 84.dp.toPx() * zoom, c1, style = Stroke(1.dp.toPx()))
            drawCircle(terrainColor.copy(alpha = 0.55f), 64.dp.toPx() * zoom, c2, style = Stroke(1.5.dp.toPx()))
        }

        // 4. Railway & Pedestrian Corridors
        routes.forEach { route ->
            if (route.waypoints.size >= 2) {
                val path = Path().apply {
                    val start = toLatLngScreen(route.waypoints[0].first, route.waypoints[0].second)
                    moveTo(start.x, start.y)
                    for (idx in 1 until route.waypoints.size) {
                        val pt = toLatLngScreen(route.waypoints[idx].first, route.waypoints[idx].second)
                        lineTo(pt.x, pt.y)
                    }
                }
                if (route.isRailway) {
                    drawPath(
                        path = path,
                        color = EmeraldPrimary.copy(alpha = 0.85f),
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawPath(
                        path = path,
                        color = SacredGoldBright,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                        )
                    )
                } else {
                    drawPath(
                        path = path,
                        color = SacredGold.copy(alpha = 0.85f),
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                        )
                    )
                }
            }
        }

        // 5. Optional Offline Hotel & User GPS Pins if inside region
        if (hotelLat != null && hotelLng != null && OfflineMapMath.isWithinRegion(hotelLat, hotelLng, region)) {
            val hPos = toLatLngScreen(hotelLat, hotelLng)
            drawCircle(Color(0xFF1565C0), 7.dp.toPx(), hPos)
            drawCircle(Color.White, 7.dp.toPx(), hPos, style = Stroke(2.dp.toPx()))
        }
        if (userLat != null && userLng != null && OfflineMapMath.isWithinRegion(userLat, userLng, region)) {
            val uPos = toLatLngScreen(userLat, userLng)
            drawCircle(Color(0xFF2196F3).copy(alpha = 0.25f), 16.dp.toPx(), uPos)
            drawCircle(Color(0xFF1976D2), 6.5.dp.toPx(), uPos)
            drawCircle(Color.White, 6.5.dp.toPx(), uPos, style = Stroke(2.dp.toPx()))
        }

        // 6. Landmark Pins & Labels
        val textPaint = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = 10.5.dp.toPx()
            typeface = Typeface.DEFAULT_BOLD
            color = labelText.toArgb()
        }

        landmarks.forEach { lm ->
            val pos = toLatLngScreen(lm.lat, lm.lng)
            if (pos.x in -30f..(w + 30f) && pos.y in -30f..(h + 30f)) {
                val isSelected = lm.id == selectedLandmarkId
                val pinColor = when {
                    isSelected -> StatusDanger
                    lm.category == LandmarkCategory.SANCTUARY -> SacredGold
                    lm.category == LandmarkCategory.RITUAL -> EmeraldPrimary
                    lm.category == LandmarkCategory.MIQAT -> Color(0xFF8E24AA)
                    else -> Color(0xFF00796B)
                }
                val radius = if (isSelected) 11.dp.toPx() else if (lm.isPrimaryHub) 9.dp.toPx() else 7.5.dp.toPx()
                if (isSelected) {
                    drawCircle(pinColor.copy(alpha = 0.24f), 22.dp.toPx(), pos)
                }
                drawCircle(pinColor, radius, pos)
                drawCircle(Color.White, radius, pos, style = Stroke(2.dp.toPx()))
                drawCircle(Color.White, radius * 0.34f, pos)

                // Label pill below pin
                val tw = textPaint.measureText(lm.shortLabel) + 12.dp.toPx()
                val th = 16.dp.toPx()
                val lx = pos.x.coerceIn(tw / 2f + 4f, (w - tw / 2f - 4f).coerceAtLeast(tw / 2f + 4f))
                val ly = (pos.y + radius + 3.dp.toPx()).coerceIn(6f, h - th - 36.dp.toPx())
                drawRoundRect(
                    color = if (isSelected) pinColor else labelBg,
                    topLeft = Offset(lx - tw / 2f, ly),
                    size = Size(tw, th),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
                textPaint.color = if (isSelected) Color.White.toArgb() else labelText.toArgb()
                drawContext.canvas.nativeCanvas.drawText(
                    lm.shortLabel,
                    lx,
                    ly + th * 0.74f,
                    textPaint
                )
            }
        }
    }
}

@Composable
private fun OfflineLandmarkDetailCard(
    landmark: SacredLandmark,
    language: AppLanguage,
    strings: OfflineMapStrings,
    userLat: Double?,
    userLng: Double?,
    onJumpTo3D: (Scene3DType) -> Unit,
    onSpeak: (String, String) -> Unit
) {
    val hubDistMeters = remember(landmark) { OfflineMapMath.distanceFromRegionHubMeters(landmark) }
    val userDistMeters = remember(landmark, userLat, userLng) {
        if (userLat != null && userLng != null) {
            GeoMath.distanceMeters(userLat, userLng, landmark.lat, landmark.lng)
        } else null
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("offline_landmark_detail_${landmark.id}"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.2.dp, SacredGold.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = landmark.photoRes),
                    contentDescription = landmark.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, SacredGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = landmark.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = landmark.subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = SacredGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.4f", landmark.lat)}, ${String.format(Locale.US, "%.4f", landmark.lng)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Offline Distance & Coordinates Metric Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricMiniCard(
                    label = strings.distFromHaramLabel,
                    value = OfflineMapMath.formatDistance(hubDistMeters, strings),
                    highlightColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (userDistMeters != null) {
                    val bearing = OfflineMapMath.bearingDegrees(userLat!!, userLng!!, landmark.lat, landmark.lng).roundToInt()
                    MetricMiniCard(
                        label = strings.distFromYouLabel,
                        value = "${OfflineMapMath.formatDistance(userDistMeters, strings)} (${bearing}°)",
                        highlightColor = StatusSuccess,
                        icon = Icons.Outlined.MyLocation,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Text(
                text = landmark.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            DuaQuoteBox(
                title = strings.tipPrefix,
                text = landmark.ritualTip
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PilgrimOutlineButton(
                    text = strings.listenBtn,
                    onClick = { onSpeak("${landmark.name}. ${landmark.description}. ${landmark.ritualTip}", language.code) },
                    modifier = Modifier.weight(1f),
                    tag = "offline_map_speak_btn"
                )
                if (landmark.scene3D != null) {
                    PilgrimGoldButton(
                        text = strings.open3DBtn,
                        onClick = { onJumpTo3D(landmark.scene3D) },
                        modifier = Modifier.weight(1f),
                        tag = "offline_map_3d_btn"
                    )
                }
            }
        }
    }
}
