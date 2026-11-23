package com.example.ui.components

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.Scene3DLabels
import com.example.model.Scene3DType
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright
import com.example.ui.theme.SacredGoldLight
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class Vec3(val x: Float, val y: Float, val z: Float)
private data class Proj2(val sx: Float, val sy: Float, val z: Float, val sc: Float)

private sealed class RenderPrimitive(val depth: Float) {
    class Poly(
        val pts: List<Proj2>,
        val fill: Color,
        val stroke: Color,
        depth: Float
    ) : RenderPrimitive(depth)

    class Line(
        val a: Proj2,
        val b: Proj2,
        val width: Float,
        val color: Color,
        val dashed: Boolean,
        depth: Float
    ) : RenderPrimitive(depth)

    class Dot(
        val pt: Proj2,
        val r: Float,
        val fill: Color,
        val label: String?,
        val haloColor: Color?,
        val pulseFactor: Float,
        depth: Float
    ) : RenderPrimitive(depth)
}

private sealed class SceneItem {
    class Poly(
        val pts: List<Vec3>,
        val fill: Color,
        val stroke: Color = Color.White.copy(alpha = 0.14f)
    ) : SceneItem()

    class Line(
        val a: Vec3,
        val b: Vec3,
        val width: Float,
        val color: Color,
        val dashed: Boolean = false
    ) : SceneItem()

    class Dot(
        val pt: Vec3,
        val r: Float,
        val fill: Color,
        val label: String? = null,
        val haloColor: Color? = null,
        val pulseFactor: Float = 0f
    ) : SceneItem()
}

private val RoadAsphaltTop = Color(0xFF2B3438)
private val RoadAsphaltSide = Color(0xFF1E2528)
private val BusAmber = Color(0xFFF59E0B)
private val BusAmberDark = Color(0xFFB45309)
private val BusLaneCyan = Color(0xFF38BDF8)
private val EmsCrimson = Color(0xFFDC2626)
private val EmsCrimsonBright = Color(0xFFEF4444)
private val WaterMistBlue = Color(0xFF0EA5E9)
private val RailEmerald = Color(0xFF10B981)

/**
 * Interactive 3D projected software renderer on Compose Canvas with architectural landmarks,
 * shuttle bus corridors, ambulance assembly hubs, rail viaducts, and essential service zones.
 */
@Composable
fun Scene3DCanvas(
    scene: Scene3DType,
    labels: Scene3DLabels,
    showServicesLayer: Boolean,
    autoOrbit: Boolean,
    yaw: Float,
    pitch: Float,
    zoom: Float,
    onOrbitDrag: (dx: Float, dy: Float) -> Unit,
    onZoomChange: (Float) -> Unit,
    onAutoOrbitTick: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var animTime by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(autoOrbit) {
        while (true) {
            delay(16L)
            animTime += 0.026f
            if (autoOrbit) {
                onAutoOrbitTick(0.004f)
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, gestureZoom, _ ->
                    if (pan.x != 0f || pan.y != 0f) {
                        onOrbitDrag(pan.x * 0.01f, pan.y * 0.008f)
                    }
                    if (gestureZoom != 1f) {
                        onZoomChange(gestureZoom)
                    }
                }
            }
    ) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF1F3D32), Color(0xFF07110E)),
                center = Offset(size.width * 0.5f, size.height * 0.3f),
                radius = size.maxDimension * 0.75f
            )
        )

        val starCoords = listOf(
            0.12f to 0.14f, 0.25f to 0.09f, 0.38f to 0.19f, 0.54f to 0.10f,
            0.69f to 0.16f, 0.83f to 0.08f, 0.91f to 0.21f, 0.07f to 0.26f
        )
        starCoords.forEachIndexed { idx, (sxRatio, syRatio) ->
            val twinkle = 0.25f + 0.35f * ((sin(animTime * 1.4f + idx) + 1f) * 0.5f)
            drawCircle(
                color = SacredGoldBright.copy(alpha = twinkle),
                radius = if (idx % 2 == 0) 2.2f else 1.5f,
                center = Offset(size.width * sxRatio, size.height * syRatio)
            )
        }

        val cx = size.width / 2f
        val cy = size.height * 0.56f
        val items = mutableListOf<SceneItem>()
        val pulseWave = 0.5f + 0.5f * sin(animTime * 2.4f)

        fun addBox(
            x: Float,
            y: Float,
            z: Float,
            w: Float,
            h: Float,
            d: Float,
            top: Color,
            side: Color,
            front: Color
        ) {
            val hw = w / 2f
            val hd = d / 2f
            val v = listOf(
                Vec3(x - hw, y, z - hd), Vec3(x + hw, y, z - hd),
                Vec3(x + hw, y, z + hd), Vec3(x - hw, y, z + hd),
                Vec3(x - hw, y + h, z - hd), Vec3(x + hw, y + h, z - hd),
                Vec3(x + hw, y + h, z + hd), Vec3(x - hw, y + h, z + hd)
            )
            items += SceneItem.Poly(listOf(v[4], v[5], v[6], v[7]), top)
            items += SceneItem.Poly(listOf(v[0], v[1], v[5], v[4]), front)
            items += SceneItem.Poly(listOf(v[2], v[3], v[7], v[6]), front)
            items += SceneItem.Poly(listOf(v[1], v[2], v[6], v[5]), side)
            items += SceneItem.Poly(listOf(v[3], v[0], v[4], v[7]), side)
        }

        fun addPyr(x: Float, y: Float, z: Float, w: Float, h: Float, d: Float, c1: Color, c2: Color) {
            val hw = w / 2f
            val hd = d / 2f
            val ap = Vec3(x, y + h, z)
            val b = listOf(
                Vec3(x - hw, y, z - hd), Vec3(x + hw, y, z - hd),
                Vec3(x + hw, y, z + hd), Vec3(x - hw, y, z + hd)
            )
            items += SceneItem.Poly(listOf(b[0], b[1], ap), c1, Color.Black.copy(alpha = 0.1f))
            items += SceneItem.Poly(listOf(b[1], b[2], ap), c2, Color.Black.copy(alpha = 0.1f))
            items += SceneItem.Poly(listOf(b[2], b[3], ap), c1, Color.Black.copy(alpha = 0.1f))
            items += SceneItem.Poly(listOf(b[3], b[0], ap), c2, Color.Black.copy(alpha = 0.1f))
        }

        fun addShuttleBus(x: Float, y: Float, z: Float, alongX: Boolean = true) {
            val bw = if (alongX) 18f else 9f
            val bd = if (alongX) 9f else 18f
            addBox(x, y, z, bw, 7.5f, bd, Color(0xFFFDE68A), BusAmberDark, BusAmber)
            addBox(x, y + 7.5f, z, bw * 0.72f, 1.8f, bd * 0.72f, Color.White, Color(0xFFD1D5DB), Color(0xFFE5E7EB))
        }

        fun addAmbulancePavilion(x: Float, z: Float, label: String) {
            // Medical triage building
            addBox(x, 0f, z, 30f, 12f, 22f, Color(0xFFF8FAFC), Color(0xFFCBD5E1), Color(0xFFE2E8F0))
            // Crimson Red Crescent / EMS roof cross strip
            addBox(x, 12f, z, 18f, 2.5f, 6f, EmsCrimsonBright, EmsCrimson, EmsCrimson)
            addBox(x, 12f, z, 6f, 2.5f, 16f, EmsCrimsonBright, EmsCrimson, EmsCrimson)
            // Parked ambulance unit alongside bay
            addBox(x - 20f, 0f, z + 2f, 13f, 6.5f, 8f, Color.White, Color(0xFFE2E8F0), Color(0xFFF1F5F9))
            addBox(x - 20f, 6.5f, z + 2f, 8f, 1.5f, 4f, EmsCrimsonBright, EmsCrimson, EmsCrimson)
            items += SceneItem.Dot(
                pt = Vec3(x, 20f, z),
                r = 5.2f,
                fill = EmsCrimsonBright,
                label = label,
                haloColor = EmsCrimson,
                pulseFactor = pulseWave
            )
        }

        fun addWaterCoolingTower(x: Float, z: Float, label: String?) {
            addBox(x, 0f, z, 14f, 4f, 14f, Color(0xFFBAE6FD), Color(0xFF0284C7), Color(0xFF38BDF8))
            addBox(x, 4f, z, 4f, 12f, 4f, Color(0xFFE0F2FE), Color(0xFF0284C7), WaterMistBlue)
            addBox(x, 16f, z, 16f, 2.5f, 16f, WaterMistBlue, Color(0xFF0369A1), Color(0xFF38BDF8))
            items += SceneItem.Dot(
                pt = Vec3(x, 22f, z),
                r = 4f,
                fill = BusLaneCyan,
                label = label,
                haloColor = WaterMistBlue,
                pulseFactor = pulseWave * 0.7f
            )
        }

        fun pointOnRectLoop(progress: Float, halfW: Float, halfD: Float): Pair<Float, Boolean> {
            // Returns offset and alongX flag mapped across perimeter [0, 4)
            val p = ((progress % 4f) + 4f) % 4f
            return when {
                p < 1f -> (-halfW + p * (2f * halfW)) to true
                p < 2f -> (-halfD + (p - 1f) * (2f * halfD)) to false
                p < 3f -> (halfW - (p - 2f) * (2f * halfW)) to true
                else -> (halfD - (p - 3f) * (2f * halfD)) to false
            }
        }

        when (scene) {
            Scene3DType.KAABA -> {
                addBox(0f, -4f, 0f, 260f, 4f, 260f, Color(0xFFDDD6C5), Color(0xFFB9B09C), Color(0xFFC8C0AE))
                addBox(0f, 0f, 0f, 56f, 68f, 56f, Color(0xFF222222), Color(0xFF141414), Color(0xFF191919))
                addBox(0f, 45f, 0f, 57f, 7f, 57f, SacredGold, SacredGold, Color(0xFFD4AF6A))
                addBox(28.5f, 12f, -8f, 2f, 24f, 13f, SacredGold, Color(0xFF9E7D3B), SacredGoldLight)
                addBox(48f, 0f, 4f, 10f, 18f, 10f, Color(0xFFD4AF6A), Color(0xFF9E7D3B), SacredGold)
                var a = 0f
                while (a <= PI.toFloat()) {
                    addBox(cos(a) * 34f, 0f, 28f + sin(a) * 26f, 7f, 10f, 7f, Color(0xFFEAE5D8), Color(0xFFC9C2B2), Color(0xFFD8D1C2))
                    a += 0.32f
                }
                listOf(-105f to -105f, 105f to -105f, -105f to 105f, 105f to 105f).forEach { (mx, mz) ->
                    addBox(mx, 0f, mz, 10f, 55f, 10f, Color(0xFFEAE5D8), Color(0xFFC9C2B2), Color(0xFFD8D1C2))
                    addPyr(mx, 55f, mz, 10f, 14f, 10f, SacredGold, Color(0xFF9E7D3B))
                }
                items += SceneItem.Dot(Vec3(28f, 14f, -28f), 5f, Color(0xFFFF5722), labels.blackStone)
                items += SceneItem.Dot(Vec3(-28f, 14f, -28f), 4f, SacredGoldLight, labels.yemeniCorner)
                items += SceneItem.Dot(Vec3(48f, 22f, 4f), 4f, SacredGold, labels.maqamIbrahim)
                for (i in 0 until 24) {
                    val ang = (i / 24f) * (2f * PI.toFloat()) + animTime * 0.7f
                    val rad = 66f + (i % 3) * 17f
                    items += SceneItem.Dot(Vec3(cos(ang) * rad, 5f, sin(ang) * rad), 3.2f, Color.White)
                }
                for (k in 0 until 4) {
                    val ang = (k / 4f) * (2f * PI.toFloat()) + animTime * 0.7f
                    items += SceneItem.Dot(Vec3(cos(ang) * 52f, 2f, sin(ang) * 52f), 4.5f, SacredGold, "↺")
                }
                if (showServicesLayer) {
                    addAmbulancePavilion(92f, 88f, labels.ambulanceHubLabel)
                    addWaterCoolingTower(-92f, 88f, labels.waterMistLabel)
                }
            }

            Scene3DType.MASAA -> {
                addBox(0f, -4f, 0f, 320f, 4f, 96f, Color(0xFFE4DEC9), Color(0xFFBDB6A2), Color(0xFFCCC5B2))
                addPyr(-130f, 0f, 0f, 55f, 42f, 65f, Color(0xFF8D7B68), Color(0xFF6F5E4D))
                addPyr(130f, 0f, 0f, 55f, 42f, 65f, Color(0xFF8D7B68), Color(0xFF6F5E4D))
                listOf(-25f to -42f, -25f to 42f, 25f to -42f, 25f to 42f).forEach { (px, pz) ->
                    addBox(px, 0f, pz, 8f, 48f, 8f, Color(0xFF1E8449), Color(0xFF145A32), Color(0xFF27AE60))
                }
                items += SceneItem.Dot(Vec3(-130f, 46f, 0f), 4f, SacredGold, labels.safa)
                items += SceneItem.Dot(Vec3(130f, 46f, 0f), 4f, SacredGold, labels.marwah)
                items += SceneItem.Dot(Vec3(0f, 52f, 0f), 4f, Color(0xFF2ECC71), labels.greenMarkers)
                for (i in 0 until 12) {
                    items += SceneItem.Dot(Vec3(sin(animTime * 0.7f + i) * 95f, 4f, if (i % 2 == 0) -14f else 14f), 3.5f, Color.White)
                }
                if (showServicesLayer) {
                    // Dedicated electric carts lane along southern side
                    addBox(0f, 0f, 34f, 210f, 1.2f, 14f, Color(0xFF164E63), Color(0xFF0E7490), Color(0xFF0891B2))
                    items += SceneItem.Line(Vec3(-100f, 2f, 34f), Vec3(100f, 2f, 34f), 2.5f, BusLaneCyan, dashed = true)
                    val cartX = sin(animTime * 0.8f) * 85f
                    addBox(cartX, 1.5f, 34f, 12f, 5f, 7f, BusLaneCyan, WaterMistBlue, BusLaneCyan)
                    items += SceneItem.Dot(Vec3(0f, 11f, 34f), 3.8f, BusLaneCyan, labels.cartsLaneLabel)
                    addAmbulancePavilion(120f, -34f, labels.ambulanceHubLabel)
                }
            }

            Scene3DType.ARAFAT -> {
                // Expanded Arafat plain
                addBox(0f, -4f, 0f, 310f, 4f, 260f, Color(0xFFD8C8A8), Color(0xFFB8A888), Color(0xFFC8B898))
                // Mount of Mercy (Jabal Al-Rahmah)
                addBox(-10f, 0f, -10f, 118f, 20f, 100f, Color(0xFF8E735B), Color(0xFF735B46), Color(0xFF826851))
                addBox(-10f, 20f, -10f, 72f, 22f, 62f, Color(0xFF9E8268), Color(0xFF7D644E), Color(0xFF8F735B))
                addBox(-10f, 42f, -10f, 10f, 28f, 10f, Color.White, Color(0xFFDDDDDD), Color(0xFFEEEEEE))
                // Masjid Namirah
                addBox(-88f, 0f, 44f, 58f, 20f, 46f, Color(0xFFE5DFD0), Color(0xFFC4BEAE), Color(0xFFD4CEBE))
                addBox(-110f, 0f, 25f, 7f, 36f, 7f, Color(0xFFEAE5D8), Color(0xFFC9C2B2), Color(0xFFD8D1C2))
                addBox(-66f, 0f, 25f, 7f, 36f, 7f, Color(0xFFEAE5D8), Color(0xFFC9C2B2), Color(0xFFD8D1C2))
                items += SceneItem.Dot(Vec3(-10f, 76f, -10f), 5f, SacredGold, labels.jabalRahmah)
                items += SceneItem.Dot(Vec3(-88f, 27f, 44f), 4.2f, Color(0xFF1E8449), labels.masjidNamirah)

                if (showServicesLayer) {
                    val loopX = 128f
                    val loopZ = 96f
                    // Perimeter Shuttle Bus Corridor (4 road segments)
                    addBox(0f, 0f, -loopZ, loopX * 2f + 16f, 1.2f, 16f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    addBox(0f, 0f, loopZ, loopX * 2f + 16f, 1.2f, 16f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    addBox(-loopX, 0f, 0f, 16f, 1.2f, loopZ * 2f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    addBox(loopX, 0f, 0f, 16f, 1.2f, loopZ * 2f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)

                    // Dashed Bus Route markings
                    items += SceneItem.Line(Vec3(-loopX, 2f, -loopZ), Vec3(loopX, 2f, -loopZ), 2.5f, BusAmber, dashed = true)
                    items += SceneItem.Line(Vec3(loopX, 2f, -loopZ), Vec3(loopX, 2f, loopZ), 2.5f, BusAmber, dashed = true)
                    items += SceneItem.Line(Vec3(loopX, 2f, loopZ), Vec3(-loopX, 2f, loopZ), 2.5f, BusAmber, dashed = true)
                    items += SceneItem.Line(Vec3(-loopX, 2f, loopZ), Vec3(-loopX, 2f, -loopZ), 2.5f, BusAmber, dashed = true)

                    // 2 Animated Shuttle Buses circling Arafat
                    listOf(0f, 2f).forEachIndexed { idx, offset ->
                        val p = ((animTime * 0.45f + offset) % 4f + 4f) % 4f
                        val bx: Float
                        val bz: Float
                        val alongX: Boolean
                        when {
                            p < 1f -> {
                                val (v, ax) = pointOnRectLoop(p, loopX, loopZ)
                                bx = v; bz = -loopZ; alongX = ax
                            }
                            p < 2f -> {
                                val (v, ax) = pointOnRectLoop(p, loopX, loopZ)
                                bx = loopX; bz = v; alongX = ax
                            }
                            p < 3f -> {
                                val (v, ax) = pointOnRectLoop(p, loopX, loopZ)
                                bx = v; bz = loopZ; alongX = ax
                            }
                            else -> {
                                val (v, ax) = pointOnRectLoop(p, loopX, loopZ)
                                bx = -loopX; bz = v; alongX = ax
                            }
                        }
                        addShuttleBus(bx, 1.2f, bz, alongX)
                        if (idx == 0) {
                            items += SceneItem.Dot(Vec3(bx, 14f, bz), 3.6f, BusAmber, labels.busRouteLabel)
                        }
                    }

                    // Bus Hub Canopy at North-West Corner
                    addBox(-96f, 0f, -96f, 28f, 10f, 18f, BusAmber, BusAmberDark, BusAmber)
                    items += SceneItem.Dot(Vec3(-96f, 15f, -96f), 4f, BusAmber, labels.busStationLabel)

                    // Ambulance Assembly Hub + Medevac Helipad (East Plain)
                    addAmbulancePavilion(84f, -52f, labels.ambulanceHubLabel)
                    addBox(84f, 0f, 16f, 26f, 1.8f, 26f, Color(0xFF475569), Color(0xFF1E293B), Color(0xFF334155))
                    addBox(84f, 1.8f, 16f, 14f, 0.8f, 4f, Color.White, Color.White, Color.White)
                    addBox(79f, 1.8f, 16f, 4f, 0.8f, 14f, Color.White, Color.White, Color.White)
                    addBox(89f, 1.8f, 16f, 4f, 0.8f, 14f, Color.White, Color.White, Color.White)
                    items += SceneItem.Dot(Vec3(84f, 10f, 16f), 4.2f, EmsCrimsonBright, labels.helipadLabel, EmsCrimson, pulseWave)

                    // Cooling Mist & Water Stations along Namirah -> Rahmah Pedestrian Path
                    addWaterCoolingTower(-48f, 10f, labels.waterMistLabel)
                    addWaterCoolingTower(34f, 48f, null)

                    // Mashaaer Rail Station along South Corridor
                    addBox(10f, 6f, 114f, 220f, 3.5f, 16f, Color(0xFF334155), Color(0xFF1E293B), Color(0xFF475569))
                    addBox(10f, 9.5f, 114f, 54f, 11f, 22f, RailEmerald, Color(0xFF047857), Color(0xFF059669))
                    val trainX = -75f + ((animTime * 28f) % 150f)
                    addBox(trainX, 9.5f, 114f, 34f, 6f, 8f, Color(0xFFECFDF5), Color(0xFF065F46), RailEmerald)
                    items += SceneItem.Dot(Vec3(10f, 26f, 114f), 4.2f, RailEmerald, labels.trainStationLabel)
                }
            }

            Scene3DType.JAMARAT -> {
                // Expanded Mina Valley
                addBox(0f, -4f, 0f, 320f, 4f, 250f, Color(0xFFDCD6C6), Color(0xFFB8B2A2), Color(0xFFCAC4B4))
                // Multi-tier Jamarat Bridge & Pillars
                addBox(0f, 12f, 0f, 224f, 6f, 50f, Color(0xFFE8E4DA), Color(0xFFC2BEB4), Color(0xFFD5D1C7))
                val jLabels = listOf(labels.jamaratSughra, labels.jamaratWusta, labels.jamaratKubra)
                listOf(-70f, 0f, 70f).forEachIndexed { idx, px ->
                    addBox(px, 18f, 0f, 14f, 32f, 24f, SacredGold, Color(0xFF8A6D34), Color(0xFFA88744))
                    items += SceneItem.Dot(Vec3(px, 56f, 0f), 4f, SacredGold, jLabels[idx])
                }
                // Mina Tents Grid (north and south blocks)
                listOf(-86f, -30f, 26f).forEach { tx ->
                    addPyr(tx, 0f, -58f, 36f, 25f, 36f, Color.White, Color(0xFFE0E0E0))
                    addPyr(tx, 0f, 58f, 36f, 25f, 36f, Color.White, Color(0xFFE0E0E0))
                }

                if (showServicesLayer) {
                    // Dedicated Bus & Emergency Avenue along North & West Mina
                    addBox(0f, 0f, -102f, 292f, 1.2f, 18f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    addBox(-136f, 0f, -2f, 18f, 1.2f, 188f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    items += SceneItem.Line(Vec3(-136f, 2f, -102f), Vec3(140f, 2f, -102f), 2.5f, BusAmber, dashed = true)
                    items += SceneItem.Line(Vec3(-136f, 2f, -102f), Vec3(-136f, 2f, 88f), 2.5f, BusAmber, dashed = true)

                    // Animated Shuttle Buses on the Mina Corridor
                    val bus1X = -118f + ((animTime * 34f) % 240f)
                    addShuttleBus(bus1X, 1.2f, -102f, alongX = true)
                    items += SceneItem.Dot(Vec3(bus1X, 14f, -102f), 3.6f, BusAmber, labels.busRouteLabel)

                    val bus2Z = -84f + ((animTime * 26f) % 164f)
                    addShuttleBus(-136f, 1.2f, bus2Z, alongX = false)

                    // Bus Hub Drop-off Canopy
                    addBox(-92f, 0f, -102f, 26f, 9f, 16f, BusAmber, BusAmberDark, BusAmber)
                    items += SceneItem.Dot(Vec3(-92f, 15f, -102f), 3.8f, BusAmber, labels.busStationLabel)

                    // Ambulance Assembly & Triage Bay beside East Jamarat Exit
                    addAmbulancePavilion(108f, -54f, labels.ambulanceHubLabel)
                    // Emergency Corridor Link to Jamarat Exit
                    items += SceneItem.Line(Vec3(108f, 2f, -42f), Vec3(108f, 2f, 0f), 2.5f, EmsCrimsonBright, dashed = true)

                    // Multilingual Guidance & Essential Services Center
                    addBox(92f, 0f, 56f, 34f, 14f, 26f, RailEmerald, Color(0xFF065F46), Color(0xFF059669))
                    addBox(92f, 14f, 56f, 36f, 2.5f, 28f, SacredGold, SacredGoldDarkCompat, SacredGoldLight)
                    items += SceneItem.Dot(Vec3(92f, 22f, 56f), 4f, SacredGoldBright, labels.servicesCenterLabel)

                    // Cooling Water & Mist Stations
                    addWaterCoolingTower(-56f, 58f, labels.waterMistLabel)

                    // Mashaaer Rail Viaduct along South Mina
                    addBox(0f, 6f, 108f, 240f, 3.5f, 16f, Color(0xFF334155), Color(0xFF1E293B), Color(0xFF475569))
                    addBox(0f, 9.5f, 108f, 52f, 10f, 20f, RailEmerald, Color(0xFF047857), Color(0xFF059669))
                    val minaTrainX = -92f + ((animTime * 30f) % 184f)
                    addBox(minaTrainX, 9.5f, 108f, 34f, 6f, 8f, Color(0xFFECFDF5), Color(0xFF065F46), RailEmerald)
                    items += SceneItem.Dot(Vec3(0f, 25f, 108f), 4.2f, RailEmerald, labels.trainStationLabel)
                }
            }

            Scene3DType.MADINAH -> {
                addBox(0f, -4f, 0f, 280f, 4f, 230f, Color(0xFFEAE5D6), Color(0xFFC9C2B2), Color(0xFFDBD5C6))
                addBox(0f, 0f, -20f, 160f, 26f, 110f, Color(0xFFEFECE2), Color(0xFFCFCAC0), Color(0xFFE2DFD5))
                addBox(45f, 26f, -35f, 34f, 14f, 34f, Color(0xFF1E8449), Color(0xFF145A32), Color(0xFF27AE60))
                addPyr(45f, 40f, -35f, 34f, 22f, 34f, Color(0xFF1E8449), Color(0xFF196F3D))
                items += SceneItem.Dot(Vec3(45f, 65f, -35f), 5f, SacredGold, labels.greenDome)
                items += SceneItem.Dot(Vec3(0f, 30f, -10f), 4f, Color(0xFF1E8449), labels.rawda)
                listOf(-85f to -80f, 85f to -80f).forEach { (mx, mz) ->
                    addBox(mx, 0f, mz, 12f, 80f, 12f, Color(0xFFEAE5D6), Color(0xFFC9C2B2), Color(0xFFDBD5C6))
                    addPyr(mx, 80f, mz, 12f, 18f, 12f, SacredGold, Color(0xFF9E7D3B))
                }
                listOf(-55f to 65f, 0f to 65f, 55f to 65f).forEach { (ux, uz) ->
                    addBox(ux, 0f, uz, 4f, 24f, 4f, SacredGold, Color(0xFF8A6D34), Color(0xFFA88744))
                    addBox(ux, 24f, uz, 36f, 3f, 36f, Color.White, Color(0xFFDCDCDC), Color(0xFFF2F2F2))
                }
                if (showServicesLayer) {
                    addBox(0f, 0f, 102f, 240f, 1.2f, 16f, RoadAsphaltTop, RoadAsphaltSide, RoadAsphaltSide)
                    items += SceneItem.Line(Vec3(-115f, 2f, 102f), Vec3(115f, 2f, 102f), 2.5f, BusAmber, dashed = true)
                    val mBusX = -85f + ((animTime * 24f) % 170f)
                    addShuttleBus(mBusX, 1.2f, 102f, alongX = true)
                    items += SceneItem.Dot(Vec3(mBusX, 14f, 102f), 3.6f, BusAmber, labels.busStationLabel)
                    addAmbulancePavilion(98f, 64f, labels.ambulanceHubLabel)
                }
            }
        }

        val cyw = cos(yaw)
        val syw = sin(yaw)
        val cp = cos(pitch)
        val sp = sin(pitch)
        val scaleFactor = (size.width / 370f).coerceIn(0.85f, 1.5f)

        fun project(v: Vec3): Proj2? {
            val x1 = v.x * cyw - v.z * syw
            val z1 = v.x * syw + v.z * cyw
            val y2 = v.y * cp + z1 * sp
            val z2 = -v.y * sp + z1 * cp
            val dist = 540f + z2
            if (dist < 40f) return null
            val sc = (430f * zoom * scaleFactor) / dist
            return Proj2(cx + x1 * sc, cy - y2 * sc, z2, sc)
        }

        val projected = mutableListOf<RenderPrimitive>()
        for (it in items) {
            when (it) {
                is SceneItem.Poly -> {
                    val pts = it.pts.mapNotNull(::project)
                    if (pts.size == it.pts.size) {
                        val depth = pts.sumOf { p -> p.z.toDouble() }.toFloat() / pts.size
                        projected += RenderPrimitive.Poly(pts, it.fill, it.stroke, depth)
                    }
                }
                is SceneItem.Line -> {
                    val pa = project(it.a)
                    val pb = project(it.b)
                    if (pa != null && pb != null) {
                        val depth = (pa.z + pb.z) / 2f - 0.5f
                        projected += RenderPrimitive.Line(pa, pb, it.width, it.color, it.dashed, depth)
                    }
                }
                is SceneItem.Dot -> {
                    val p = project(it.pt)
                    if (p != null) {
                        projected += RenderPrimitive.Dot(
                            pt = p,
                            r = it.r,
                            fill = it.fill,
                            label = it.label,
                            haloColor = it.haloColor,
                            pulseFactor = it.pulseFactor,
                            depth = p.z - 1f
                        )
                    }
                }
            }
        }

        projected.sortByDescending { it.depth }

        val labelPaint = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = 10.5f * density
            color = android.graphics.Color.WHITE
            typeface = Typeface.DEFAULT_BOLD
        }
        val pillBgPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.argb(185, 10, 22, 18)
        }
        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), animTime * 35f)

        for (prim in projected) {
            when (prim) {
                is RenderPrimitive.Poly -> {
                    val path = Path().apply {
                        moveTo(prim.pts[0].sx, prim.pts[0].sy)
                        for (i in 1 until prim.pts.size) lineTo(prim.pts[i].sx, prim.pts[i].sy)
                        close()
                    }
                    drawPath(path, prim.fill)
                    drawPath(path, prim.stroke, style = Stroke(width = 1f))
                }
                is RenderPrimitive.Line -> {
                    drawLine(
                        color = prim.color,
                        start = Offset(prim.a.sx, prim.a.sy),
                        end = Offset(prim.b.sx, prim.b.sy),
                        strokeWidth = prim.width * maxOf(0.8f, (prim.a.sc + prim.b.sc) * 0.5f),
                        cap = StrokeCap.Round,
                        pathEffect = if (prim.dashed) dashedEffect else null
                    )
                }
                is RenderPrimitive.Dot -> {
                    val radius = prim.r * maxOf(0.75f, prim.pt.sc)
                    if (prim.haloColor != null) {
                        val haloR = radius * (1.7f + prim.pulseFactor * 0.9f)
                        drawCircle(
                            color = prim.haloColor.copy(alpha = 0.35f * (1f - prim.pulseFactor * 0.45f)),
                            radius = haloR,
                            center = Offset(prim.pt.sx, prim.pt.sy)
                        )
                    }
                    drawCircle(prim.fill, radius = radius, center = Offset(prim.pt.sx, prim.pt.sy))
                    prim.label?.let { lbl ->
                        val textW = labelPaint.measureText(lbl)
                        val padH = 5f * density
                        val textY = prim.pt.sy - radius - 6f * density
                        val rect = RectF(
                            prim.pt.sx - textW / 2f - padH,
                            textY - labelPaint.textSize - 2f * density,
                            prim.pt.sx + textW / 2f + padH,
                            textY + 4f * density
                        )
                        drawContext.canvas.nativeCanvas.drawRoundRect(
                            rect,
                            6f * density,
                            6f * density,
                            pillBgPaint
                        )
                        labelPaint.color = prim.fill.toArgb()
                        drawContext.canvas.nativeCanvas.drawText(
                            lbl,
                            prim.pt.sx,
                            textY,
                            labelPaint
                        )
                    }
                }
            }
        }
    }
}

private val SacredGoldDarkCompat = Color(0xFF9B7B3A)
