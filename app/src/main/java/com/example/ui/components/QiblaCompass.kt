package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.outlined.RotateLeft
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GlobalUiStrings
import com.example.model.AppLanguage
import com.example.model.CompassAccuracy
import com.example.model.CompassReading
import com.example.model.QiblaLocationSource
import com.example.model.QiblaReferenceCity
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright
import com.example.ui.theme.SacredGoldDark
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningText
import com.example.util.CompassMath
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun QiblaCompassCard(
    strings: GlobalUiStrings,
    language: AppLanguage,
    compassReading: CompassReading,
    qiblaActive: Boolean,
    qiblaBearingDeg: Float?,
    qiblaDistanceMeters: Double?,
    locationSource: QiblaLocationSource,
    selectedCity: QiblaReferenceCity?,
    onActivateGps: () -> Unit,
    onSelectReferenceCity: (QiblaReferenceCity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalibrationDialog by remember { mutableStateOf(false) }

    val sensorsAvailable = compassReading.hasAccelerometer && compassReading.hasMagnetometer
    val interference = sensorsAvailable &&
        compassReading.hasInitialReading &&
        CompassMath.hasMagneticInterference(compassReading.magneticFieldMicroTesla)

    val rawAzimuth = compassReading.trueAzimuthDeg
    var continuousAzimuth by remember { mutableFloatStateOf(rawAzimuth) }
    LaunchedEffect(rawAzimuth) {
        continuousAzimuth += CompassMath.shortestAngleDelta(continuousAzimuth, rawAzimuth)
    }
    val animatedAzimuth by animateFloatAsState(
        targetValue = continuousAzimuth,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "qibla_rose_azimuth"
    )

    val relDeltaDeg = if (qiblaBearingDeg != null) {
        CompassMath.shortestAngleDelta(rawAzimuth, qiblaBearingDeg)
    } else null

    val isAligned = qiblaActive &&
        compassReading.hasInitialReading &&
        qiblaBearingDeg != null &&
        CompassMath.isAlignedWithQibla(rawAzimuth, qiblaBearingDeg)

    val isFlat = abs(compassReading.pitchDeg) <= 12f && abs(compassReading.rollDeg) <= 12f

    val accuracyLabel = when (compassReading.accuracy) {
        CompassAccuracy.HIGH -> strings.qiblaAccuracyHigh
        CompassAccuracy.MEDIUM -> strings.qiblaAccuracyMed
        CompassAccuracy.LOW, CompassAccuracy.UNRELIABLE -> strings.qiblaAccuracyLow
    }
    val accuracyColor = when (compassReading.accuracy) {
        CompassAccuracy.HIGH -> StatusSuccess
        CompassAccuracy.MEDIUM -> SacredGoldDark
        CompassAccuracy.LOW, CompassAccuracy.UNRELIABLE -> StatusDanger
    }

    PilgrimCard(
        modifier = modifier,
        accentStartBar = true
    ) {
        // Header + Sensor Calibration Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(
                title = strings.qiblaTitle,
                icon = Icons.Outlined.Explore,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("calibrate_qibla_btn")
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showCalibrationDialog = true },
                shape = RoundedCornerShape(12.dp),
                color = accuracyColor.copy(alpha = 0.13f),
                border = BorderStroke(1.dp, accuracyColor.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = strings.qiblaCalibrateTitle,
                        tint = accuracyColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = accuracyLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accuracyColor,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sensor unavailable or magnetic interference alert
        if (!sensorsAvailable) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = StatusDangerContainer,
                border = BorderStroke(1.dp, StatusDanger.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = StatusDanger,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = strings.qiblaSensorsUnavailable,
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusDanger,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else if (interference || compassReading.accuracy == CompassAccuracy.LOW || compassReading.accuracy == CompassAccuracy.UNRELIABLE) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showCalibrationDialog = true },
                shape = RoundedCornerShape(12.dp),
                color = StatusWarningContainer,
                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = StatusWarningText,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = strings.qiblaInterferenceWarning,
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusWarningText,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Compass Rose Dial
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QiblaDialCanvas(
                animatedAzimuthDeg = animatedAzimuth,
                qiblaBearingDeg = qiblaBearingDeg,
                pitchDeg = compassReading.pitchDeg,
                rollDeg = compassReading.rollDeg,
                isAligned = isAligned,
                isFlat = isFlat,
                cardinals = strings.qiblaCardinalLabels
            )

            // Digital Heading & Turn Guidance Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val headingDeg = rawAzimuth.roundToInt() % 360
                val headingCode = CompassMath.cardinalDirectionCode(rawAzimuth)
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Navigation,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${strings.qiblaHeadingLabel}: $headingDeg° $headingCode",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Alignment / Turn Guidance Pill
                val guideBg by animateColorAsState(
                    targetValue = when {
                        isAligned -> StatusSuccess.copy(alpha = 0.16f)
                        qiblaBearingDeg != null -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    label = "guide_bg"
                )
                val guideTextColor = when {
                    isAligned -> StatusSuccess
                    qiblaBearingDeg != null -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                val guideBorder = if (isAligned) {
                    StatusSuccess.copy(alpha = 0.6f)
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                }

                Surface(
                    modifier = Modifier
                        .weight(1.15f)
                        .testTag("qibla_guidance_pill"),
                    shape = RoundedCornerShape(12.dp),
                    color = guideBg,
                    border = BorderStroke(1.dp, guideBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        when {
                            isAligned -> {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = strings.qiblaAligned,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = StatusSuccess,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            relDeltaDeg != null -> {
                                val turnRight = relDeltaDeg >= 0f
                                val degInt = abs(relDeltaDeg).roundToInt()
                                Icon(
                                    imageVector = if (turnRight) Icons.AutoMirrored.Outlined.RotateRight else Icons.AutoMirrored.Outlined.RotateLeft,
                                    contentDescription = null,
                                    tint = guideTextColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "${if (turnRight) strings.qiblaTurnRightSuffix else strings.qiblaTurnLeftSuffix} $degInt°",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = guideTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            else -> {
                                Text(
                                    text = strings.qiblaPrompt,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = guideTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Spirit-level status hint
            if (sensorsAvailable) {
                Text(
                    text = if (isFlat) "✓ ${strings.qiblaLevelFlatLabel}" else strings.qiblaHoldFlatHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isFlat) StatusSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isFlat) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            // Telemetry Mini Cards Row (Bearing, Distance to Kaaba, Magnetic Field)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val bearingValue = qiblaBearingDeg?.let { "${it.roundToInt()}°" } ?: "--°"
                val distValue = qiblaDistanceMeters?.let { d ->
                    if (d >= 1000.0) "${String.format(Locale.US, "%.0f", d / 1000.0)} ${strings.kmUnit}"
                    else "${d.roundToInt()} m"
                } ?: "--"
                val fieldValue = if (compassReading.hasInitialReading) {
                    "${compassReading.magneticFieldMicroTesla.roundToInt()} µT"
                } else {
                    "-- µT"
                }

                MetricMiniCard(
                    label = strings.qiblaBearingPrefix.trimEnd(':'),
                    value = bearingValue,
                    highlightColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MetricMiniCard(
                    label = strings.qiblaToKaabaSuffix,
                    value = distValue,
                    highlightColor = SacredGoldDark,
                    modifier = Modifier.weight(1f)
                )
                MetricMiniCard(
                    label = strings.qiblaMagFieldLabel,
                    value = fieldValue,
                    highlightColor = if (interference) StatusDanger else MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Location Source Badge & Live GPS Action Button
            val sourceLabel = when (locationSource) {
                QiblaLocationSource.GPS -> strings.qiblaGpsActiveBadge
                QiblaLocationSource.SAVED -> "${strings.qiblaGpsActiveBadge} (Saved)"
                QiblaLocationSource.CITY -> {
                    val cityName = selectedCity?.let {
                        if (language == AppLanguage.AR || language == AppLanguage.UR) it.arName else it.enName
                    }.orEmpty()
                    "${strings.qiblaCityFallbackBadge}: $cityName"
                }
                QiblaLocationSource.NONE -> null
            }

            if (sourceLabel != null) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (locationSource == QiblaLocationSource.CITY) Icons.Outlined.LocationCity else Icons.Outlined.GpsFixed,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = sourceLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            PilgrimOutlineButton(
                text = if (locationSource == QiblaLocationSource.GPS) strings.qiblaRefreshGpsBtn else strings.activateQiblaBtn,
                onClick = onActivateGps,
                modifier = Modifier.fillMaxWidth(),
                tag = "activate_qibla_btn"
            )

            // Reference City Fallback Strip
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = strings.qiblaPickCityHint,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QiblaReferenceCity.entries.forEach { city ->
                        val label = if (language == AppLanguage.AR || language == AppLanguage.UR) {
                            city.arName
                        } else {
                            city.enName
                        }
                        PilgrimChip(
                            label = label,
                            selected = locationSource == QiblaLocationSource.CITY && selectedCity == city,
                            onClick = { onSelectReferenceCity(city) },
                            tag = "qibla_city_${city.name.lowercase()}"
                        )
                    }
                }
            }
        }
    }

    if (showCalibrationDialog) {
        AlertDialog(
            onDismissRequest = { showCalibrationDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Sensors,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(
                    text = strings.qiblaCalibrateTitle,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = strings.qiblaCalibrateDesc,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${strings.qiblaMagFieldLabel}: ${compassReading.magneticFieldMicroTesla.roundToInt()} µT",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Magnetometer: ${if (compassReading.hasMagnetometer) "OK" else "N/A"} • Accelerometer: ${if (compassReading.hasAccelerometer) "OK" else "N/A"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCalibrationDialog = false }) {
                    Text(strings.understoodBtn, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun QiblaDialCanvas(
    animatedAzimuthDeg: Float,
    qiblaBearingDeg: Float?,
    pitchDeg: Float,
    rollDeg: Float,
    isAligned: Boolean,
    isFlat: Boolean,
    cardinals: List<String>
) {
    val infiniteTransition = rememberInfiniteTransition(label = "qibla_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse_alpha"
    )

    val ringColor by animateColorAsState(
        targetValue = if (isAligned) StatusSuccess else SacredGold,
        label = "ring_color"
    )
    val dialSurface = MaterialTheme.colorScheme.surface
    val dialInner = MaterialTheme.colorScheme.surfaceVariant
    val tickColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val levelBubbleColor = if (isFlat) StatusSuccess else SacredGold

    Box(
        modifier = Modifier
            .size(254.dp)
            .testTag("qibla_compass_canvas"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            val rimRadius = radius - 14.dp.toPx()

            // Top Fixed Lubber Line (Phone heading indicator at 12 o'clock)
            val lubberPath = Path().apply {
                moveTo(center.x, 2.dp.toPx())
                lineTo(center.x - 8.dp.toPx(), 13.dp.toPx())
                lineTo(center.x + 8.dp.toPx(), 13.dp.toPx())
                close()
            }
            drawPath(lubberPath, color = ringColor)

            // Outer Compass Bezel & Radial Dial Face
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(dialSurface, dialInner),
                    center = center,
                    radius = rimRadius
                ),
                radius = rimRadius,
                center = center
            )

            // Alignment Glow Ring
            if (isAligned) {
                drawCircle(
                    color = StatusSuccess.copy(alpha = pulseAlpha * 0.35f),
                    radius = rimRadius - 2.dp.toPx(),
                    center = center,
                    style = Stroke(width = 10.dp.toPx())
                )
            }

            // Outer Gold/Emerald Bezel Stroke
            drawCircle(
                color = ringColor,
                radius = rimRadius,
                center = center,
                style = Stroke(width = 3.5.dp.toPx())
            )
            drawCircle(
                color = ringColor.copy(alpha = 0.28f),
                radius = rimRadius - 6.dp.toPx(),
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // Rotating Compass Rose (-animatedAzimuthDeg so North points to true North)
            rotate(degrees = -animatedAzimuthDeg, pivot = center) {
                // Degree Ticks every 5 degrees
                for (deg in 0 until 360 step 5) {
                    val isCardinal = deg % 90 == 0
                    val isMajor = deg % 30 == 0
                    val isMedium = deg % 15 == 0
                    val tickOuter = rimRadius - 8.dp.toPx()
                    val tickLen = when {
                        isCardinal -> 13.dp.toPx()
                        isMajor -> 10.dp.toPx()
                        isMedium -> 7.dp.toPx()
                        else -> 4.dp.toPx()
                    }
                    val tickInner = tickOuter - tickLen
                    val rad = Math.toRadians((deg - 90).toDouble())
                    val start = Offset(
                        center.x + (tickInner * cos(rad)).toFloat(),
                        center.y + (tickInner * sin(rad)).toFloat()
                    )
                    val end = Offset(
                        center.x + (tickOuter * cos(rad)).toFloat(),
                        center.y + (tickOuter * sin(rad)).toFloat()
                    )
                    val color = when {
                        deg == 0 -> StatusDanger
                        isCardinal -> ringColor
                        isMajor -> textColor.copy(alpha = 0.72f)
                        else -> tickColor.copy(alpha = 0.42f)
                    }
                    val strokeW = when {
                        isCardinal -> 2.6.dp.toPx()
                        isMajor -> 1.8.dp.toPx()
                        else -> 1.0.dp.toPx()
                    }
                    drawLine(color = color, start = start, end = end, strokeWidth = strokeW, cap = StrokeCap.Round)
                }

                // Cardinal & Degree Labels
                val cardinalPaint = Paint().apply {
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                    textSize = 15.sp.toPx()
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                val degreePaint = Paint().apply {
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                    textSize = 9.5.sp.toPx()
                    color = tickColor.copy(alpha = 0.75f).toArgb()
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }

                val labelRadius = rimRadius - 30.dp.toPx()
                for (deg in 0 until 360 step 30) {
                    val rad = Math.toRadians((deg - 90).toDouble())
                    val lx = center.x + (labelRadius * cos(rad)).toFloat()
                    val ly = center.y + (labelRadius * sin(rad)).toFloat()

                    if (deg % 90 == 0) {
                        val cIdx = (deg / 90) % 4
                        val label = cardinals.getOrElse(cIdx) { listOf("N", "E", "S", "W")[cIdx] }
                        cardinalPaint.color = if (deg == 0) StatusDanger.toArgb() else textColor.toArgb()
                        drawContext.canvas.nativeCanvas.drawText(
                            label,
                            lx,
                            ly + 5.dp.toPx(),
                            cardinalPaint
                        )
                    } else {
                        drawContext.canvas.nativeCanvas.drawText(
                            deg.toString(),
                            lx,
                            ly + 3.5.dp.toPx(),
                            degreePaint
                        )
                    }
                }

                // Qibla Bearing Pointer & Kaaba Badge
                if (qiblaBearingDeg != null) {
                    rotate(degrees = qiblaBearingDeg, pivot = center) {
                        val beamColor = if (isAligned) StatusSuccess else SacredGold
                        val tipY = center.y - (rimRadius - 18.dp.toPx())

                        // Translucent directional beam wedge
                        val wedge = Path().apply {
                            moveTo(center.x, center.y)
                            lineTo(center.x - 16.dp.toPx(), tipY + 14.dp.toPx())
                            lineTo(center.x + 16.dp.toPx(), tipY + 14.dp.toPx())
                            close()
                        }
                        drawPath(
                            wedge,
                            brush = Brush.verticalGradient(
                                colors = listOf(beamColor.copy(alpha = 0.36f), Color.Transparent),
                                startY = tipY,
                                endY = center.y
                            )
                        )

                        // Main Qibla Needle
                        val needleMain = Path().apply {
                            moveTo(center.x, tipY + 10.dp.toPx())
                            lineTo(center.x - 7.5.dp.toPx(), center.y)
                            lineTo(center.x, center.y - 6.dp.toPx())
                            lineTo(center.x + 7.5.dp.toPx(), center.y)
                            close()
                        }
                        drawPath(needleMain, color = beamColor)

                        // Tail Needle
                        val needleTail = Path().apply {
                            moveTo(center.x, center.y + 34.dp.toPx())
                            lineTo(center.x - 5.dp.toPx(), center.y)
                            lineTo(center.x + 5.dp.toPx(), center.y)
                            close()
                        }
                        drawPath(needleTail, color = tickColor.copy(alpha = 0.35f))

                        // Miniature Vector Kaaba Badge at tip
                        val kaabaSize = 16.dp.toPx()
                        val kaabaTopLeft = Offset(center.x - kaabaSize / 2f, tipY - 4.dp.toPx())
                        if (isAligned) {
                            drawCircle(
                                color = StatusSuccess.copy(alpha = pulseAlpha),
                                radius = kaabaSize * 0.85f,
                                center = Offset(center.x, kaabaTopLeft.y + kaabaSize / 2f)
                            )
                        }
                        drawRoundRect(
                            color = Color(0xFF141414),
                            topLeft = kaabaTopLeft,
                            size = Size(kaabaSize, kaabaSize),
                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                        )
                        drawRoundRect(
                            color = SacredGoldBright,
                            topLeft = kaabaTopLeft,
                            size = Size(kaabaSize, kaabaSize),
                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                            style = Stroke(width = 1.4.dp.toPx())
                        )
                        // Kiswah Gold Band
                        drawRect(
                            color = SacredGoldBright,
                            topLeft = Offset(kaabaTopLeft.x + 1.dp.toPx(), kaabaTopLeft.y + kaabaSize * 0.28f),
                            size = Size(kaabaSize - 2.dp.toPx(), 2.4.dp.toPx())
                        )
                    }
                }
            }

            // Center Spirit-Level Reticle (driven by Accelerometer pitch & roll)
            val reticleRadius = 22.dp.toPx()
            drawCircle(
                color = dialSurface.copy(alpha = 0.92f),
                radius = reticleRadius,
                center = center
            )
            drawCircle(
                color = levelBubbleColor.copy(alpha = 0.55f),
                radius = reticleRadius,
                center = center,
                style = Stroke(width = 1.4.dp.toPx())
            )
            // Crosshair lines
            drawLine(
                color = tickColor.copy(alpha = 0.3f),
                start = Offset(center.x - reticleRadius + 4.dp.toPx(), center.y),
                end = Offset(center.x + reticleRadius - 4.dp.toPx(), center.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = tickColor.copy(alpha = 0.3f),
                start = Offset(center.x, center.y - reticleRadius + 4.dp.toPx()),
                end = Offset(center.x, center.y + reticleRadius - 4.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )

            // Tilt Bubble
            val maxBubbleOffset = reticleRadius - 6.dp.toPx()
            val bx = (-rollDeg / 40f).coerceIn(-1f, 1f) * maxBubbleOffset
            val by = (pitchDeg / 40f).coerceIn(-1f, 1f) * maxBubbleOffset
            drawCircle(
                color = levelBubbleColor,
                radius = 5.dp.toPx(),
                center = Offset(center.x + bx, center.y + by)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = 1.8.dp.toPx(),
                center = Offset(center.x + bx - 1.2.dp.toPx(), center.y + by - 1.2.dp.toPx())
            )
        }
    }
}
