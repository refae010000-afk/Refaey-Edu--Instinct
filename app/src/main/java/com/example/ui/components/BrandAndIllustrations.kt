package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.model.Scene3DType
import com.example.model.StepIllustrationKey
import com.example.model.WeatherCity
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright
import com.example.ui.theme.SacredGoldDark
import com.example.ui.theme.SacredGoldLight

/**
 * Custom vector Kaaba & 8-point star emblem drawn natively on Compose Canvas.
 */
@Composable
fun PilgrimBrandEmblem(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val sx = w / 120f
        val sy = h / 120f

        drawRoundRect(
            brush = Brush.linearGradient(listOf(EmeraldLight, EmeraldDark)),
            cornerRadius = CornerRadius(26f * sx, 26f * sy)
        )
        drawRoundRect(
            brush = Brush.linearGradient(listOf(SacredGoldBright, SacredGoldDark)),
            topLeft = Offset(6f * sx, 6f * sy),
            size = Size(108f * sx, 108f * sy),
            cornerRadius = CornerRadius(22f * sx, 22f * sy),
            style = Stroke(width = 2.5f * sx),
            alpha = 0.75f
        )

        val star = Path().apply {
            val pts = listOf(
                60f to 14f, 73f to 32f, 95f to 32f, 82f to 50f,
                95f to 68f, 73f to 68f, 60f to 86f, 47f to 68f,
                25f to 68f, 38f to 50f, 25f to 32f, 47f to 32f
            )
            moveTo(pts[0].first * sx, pts[0].second * sy)
            for (i in 1 until pts.size) lineTo(pts[i].first * sx, pts[i].second * sy)
            close()
        }
        drawPath(star, color = SacredGold.copy(alpha = 0.45f), style = Stroke(width = 2f * sx))

        // Kaaba top
        val topPath = Path().apply {
            moveTo(60f * sx, 34f * sy)
            lineTo(86f * sx, 47f * sy)
            lineTo(60f * sx, 60f * sy)
            lineTo(34f * sx, 47f * sy)
            close()
        }
        drawPath(topPath, color = Color(0xFF2A2A2A))
        drawPath(topPath, color = SacredGoldLight, style = Stroke(width = 1.5f * sx))

        // Left wall
        val leftWall = Path().apply {
            moveTo(34f * sx, 47f * sy)
            lineTo(60f * sx, 60f * sy)
            lineTo(60f * sx, 92f * sy)
            lineTo(34f * sx, 79f * sy)
            close()
        }
        drawPath(leftWall, color = Color(0xFF141414))

        // Right wall
        val rightWall = Path().apply {
            moveTo(60f * sx, 60f * sy)
            lineTo(86f * sx, 47f * sy)
            lineTo(86f * sx, 79f * sy)
            lineTo(60f * sx, 92f * sy)
            close()
        }
        drawPath(rightWall, color = Color(0xFF1F1F1F))

        // Gold Kiswah bands & door
        val bandLeft = Path().apply {
            moveTo(34f * sx, 54f * sy); lineTo(60f * sx, 67f * sy)
            lineTo(60f * sx, 72f * sy); lineTo(34f * sx, 59f * sy); close()
        }
        val bandRight = Path().apply {
            moveTo(60f * sx, 67f * sy); lineTo(86f * sx, 54f * sy)
            lineTo(86f * sx, 59f * sy); lineTo(60f * sx, 72f * sy); close()
        }
        val door = Path().apply {
            moveTo(68f * sx, 69f * sy); lineTo(75f * sx, 65.5f * sy)
            lineTo(75f * sx, 80f * sy); lineTo(68f * sx, 83.5f * sy); close()
        }
        drawPath(bandLeft, color = SacredGoldLight)
        drawPath(bandRight, color = SacredGoldLight)
        drawPath(door, color = SacredGoldBright)

        // Crescent moon above Kaaba
        drawCircle(
            color = SacredGoldBright,
            radius = 6.5f * sx,
            center = Offset(60f * sx, 22f * sy)
        )
        drawCircle(
            color = EmeraldDark,
            radius = 5.2f * sx,
            center = Offset(62.5f * sx, 20.5f * sy)
        )
    }
}

@DrawableRes
fun holySitePhotoRes(scene: Scene3DType): Int = when (scene) {
    Scene3DType.KAABA -> R.drawable.img_kaaba_haram
    Scene3DType.MASAA -> R.drawable.img_safa_marwa
    Scene3DType.ARAFAT -> R.drawable.img_arafat_mount
    Scene3DType.JAMARAT -> R.drawable.img_mina_jamarat
    Scene3DType.MADINAH -> R.drawable.img_madinah_nabawi
}

@DrawableRes
fun weatherCityPhotoRes(city: WeatherCity): Int = when (city) {
    WeatherCity.MAKKAH, WeatherCity.MY_GPS -> R.drawable.img_kaaba_haram
    WeatherCity.MADINAH -> R.drawable.img_madinah_nabawi
    WeatherCity.ARAFAT -> R.drawable.img_arafat_mount
}

@DrawableRes
fun stepPhotoRes(key: StepIllustrationKey): Int = when (key) {
    StepIllustrationKey.IHRAM, StepIllustrationKey.TAWAF -> R.drawable.img_kaaba_haram
    StepIllustrationKey.SAI, StepIllustrationKey.HALQ -> R.drawable.img_safa_marwa
    StepIllustrationKey.MINA, StepIllustrationKey.MUZDALIFAH, StepIllustrationKey.JAMARAT -> R.drawable.img_mina_jamarat
    StepIllustrationKey.ARAFAT -> R.drawable.img_arafat_mount
    StepIllustrationKey.MADINAH -> R.drawable.img_madinah_nabawi
}

/**
 * Rich photographic + instructional vector diagram Canvas for each of the 9 ritual steps.
 */
@Composable
fun RitualStepIllustration(
    key: StepIllustrationKey,
    modifier: Modifier = Modifier,
    language: com.example.model.AppLanguage = com.example.model.AppLanguage.AR
) {
    val labels = com.example.data.LocalizedRitualGuideData.illustrationLabelsFor(language, key)
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = stepPhotoRes(key)),
            contentDescription = key.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val sx = w / 320f
            val sy = h / 110f

            // Atmospheric dark emerald scrim so instructional diagrams stay sharp over photography
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF07231A).copy(alpha = 0.56f),
                        Color(0xFF061B14).copy(alpha = 0.78f)
                    )
                )
            )

        fun drawLabel(txt: String, x: Float, y: Float, sizeSp: Float, color: Color, bold: Boolean = false) {
            drawContext.canvas.nativeCanvas.drawText(
                txt,
                x * sx,
                y * sy,
                Paint().apply {
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                    textSize = sizeSp * sy
                    this.color = color.toArgb()
                    typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                }
            )
        }

        when (key) {
            StepIllustrationKey.IHRAM -> {
                drawRoundRect(Color.White.copy(alpha = 0.95f), Offset(40f * sx, 25f * sy), Size(44f * sx, 62f * sy), CornerRadius(8f * sx, 8f * sy))
                drawRoundRect(Color.White.copy(alpha = 0.95f), Offset(92f * sx, 25f * sy), Size(44f * sx, 62f * sy), CornerRadius(8f * sx, 8f * sy))
                drawLine(Color(0xFFD9D4C7), Offset(92f * sx, 35f * sy), Offset(136f * sx, 55f * sy), strokeWidth = 4f * sx)
                drawCircle(SacredGold.copy(alpha = 0.25f), 18f * sx, Offset(220f * sx, 40f * sy))
                drawLabel(labels.getOrElse(0) { "" }, 225f, 55f, 15f, SacredGoldBright, true)
                drawLabel(labels.getOrElse(1) { "" }, 225f, 78f, 11.5f, Color.White)
            }
            StepIllustrationKey.TAWAF -> {
                drawCircle(
                    color = SacredGold,
                    radius = 45f * sy,
                    center = Offset(160f * sx, 55f * sy),
                    style = Stroke(width = 2f * sx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
                )
                drawArc(
                    color = SacredGoldLight,
                    startAngle = -90f,
                    sweepAngle = -90f,
                    useCenter = false,
                    topLeft = Offset(160f * sx - 45f * sy, 10f * sy),
                    size = Size(90f * sy, 90f * sy),
                    style = Stroke(width = 3.5f * sx, cap = StrokeCap.Round)
                )
                drawRect(Color(0xFF111111), Offset(142f * sx, 37f * sy), Size(36f * sx, 36f * sy))
                drawRect(SacredGold, Offset(142f * sx, 37f * sy), Size(36f * sx, 36f * sy), style = Stroke(2f * sx))
                drawRect(SacredGold, Offset(142f * sx, 44f * sy), Size(36f * sx, 5f * sy))
                drawCircle(Color(0xFFFF5722), 4f * sx, Offset(178f * sx, 73f * sy))
                drawLabel(labels.getOrElse(0) { "" }, 66f, 58f, 10.5f, Color.White, true)
                drawLabel(labels.getOrElse(1) { "" }, 254f, 58f, 10.5f, SacredGoldBright)
            }
            StepIllustrationKey.SAI -> {
                val safa = Path().apply { moveTo(20f * sx, 90f * sy); lineTo(55f * sx, 35f * sy); lineTo(90f * sx, 90f * sy); close() }
                val marwa = Path().apply { moveTo(230f * sx, 90f * sy); lineTo(265f * sx, 35f * sy); lineTo(300f * sx, 90f * sy); close() }
                drawPath(safa, Color(0xFF8D7B68))
                drawPath(marwa, Color(0xFF8D7B68))
                drawRoundRect(Color(0xFFEAE5D8), Offset(85f * sx, 78f * sy), Size(150f * sx, 12f * sy), CornerRadius(4f * sx, 4f * sy))
                drawRect(Color(0xFF2ECC71), Offset(140f * sx, 25f * sy), Size(8f * sx, 55f * sy))
                drawRect(Color(0xFF2ECC71), Offset(172f * sx, 25f * sy), Size(8f * sx, 55f * sy))
                drawRect(Color(0xFF27AE60), Offset(140f * sx, 25f * sy), Size(40f * sx, 7f * sy))
                drawLabel(labels.getOrElse(0) { "" }, 55f, 103f, 11f, SacredGoldBright, true)
                drawLabel(labels.getOrElse(1) { "" }, 160f, 19f, 10.5f, Color(0xFF2ECC71), true)
                drawLabel(labels.getOrElse(2) { "" }, 265f, 103f, 11f, SacredGoldBright, true)
            }
            StepIllustrationKey.HALQ -> {
                drawCircle(SacredGold.copy(alpha = 0.2f), 28f * sy, Offset(85f * sx, 55f * sy))
                drawCircle(SacredGold, 28f * sy, Offset(85f * sx, 55f * sy), style = Stroke(2f * sx))
                drawLabel("✂️", 85f, 64f, 24f, Color.White)
                drawLabel(labels.getOrElse(0) { "" }, 215f, 50f, 12.5f, SacredGoldBright, true)
                drawLabel(labels.getOrElse(1) { "" }, 215f, 72f, 10.5f, Color.White)
            }
            StepIllustrationKey.MINA -> {
                listOf(30f, 95f, 160f, 225f).forEachIndexed { idx, x ->
                    val p = Path().apply {
                        moveTo(x * sx, 85f * sy)
                        lineTo((x + 35f) * sx, 35f * sy)
                        lineTo((x + 70f) * sx, 85f * sy)
                        close()
                    }
                    drawPath(p, if (idx % 2 == 0) Color.White else Color(0xFFF2EFE9))
                }
                drawLabel(labels.getOrElse(0) { "" }, 160f, 23f, 11.5f, SacredGoldBright, true)
            }
            StepIllustrationKey.ARAFAT -> {
                drawCircle(SacredGoldBright.copy(alpha = 0.85f), 16f * sy, Offset(265f * sx, 30f * sy))
                val mt = Path().apply { moveTo(65f * sx, 92f * sy); lineTo(160f * sx, 34f * sy); lineTo(255f * sx, 92f * sy); close() }
                drawPath(mt, Color(0xFF8E735B))
                drawRect(Color.White, Offset(155f * sx, 12f * sy), Size(10f * sx, 24f * sy))
                drawLabel(labels.getOrElse(0) { "" }, 160f, 105f, 11f, SacredGoldBright, true)
            }
            StepIllustrationKey.MUZDALIFAH -> {
                drawCircle(SacredGoldBright, 12f * sy, Offset(62f * sx, 28f * sy))
                drawCircle(Color(0xFF0A192F), 10f * sy, Offset(67f * sx, 25f * sy))
                drawCircle(Color.White, 2f * sx, Offset(110f * sx, 24f * sy))
                drawCircle(Color.White, 2f * sx, Offset(240f * sx, 28f * sy))
                listOf(130f to 82f, 145f to 85f, 160f to 81f, 176f to 84f, 192f to 82f).forEach { (px, py) ->
                    drawCircle(Color(0xFFB5AEA3), 4.5f * sx, Offset(px * sx, py * sy))
                }
                drawLabel(labels.getOrElse(0) { "" }, 170f, 54f, 12f, Color.White, true)
            }
            StepIllustrationKey.JAMARAT -> {
                drawRoundRect(Color(0xFFD5D1C7), Offset(40f * sx, 75f * sy), Size(240f * sx, 14f * sy), CornerRadius(4f * sx, 4f * sy))
                drawRect(SacredGold, Offset(75f * sx, 34f * sy), Size(18f * sx, 41f * sy))
                drawRect(SacredGold, Offset(151f * sx, 30f * sy), Size(18f * sx, 45f * sy))
                drawRect(SacredGold, Offset(227f * sx, 26f * sy), Size(18f * sx, 49f * sy))
                val arcPath = Path().apply {
                    moveTo(40f * sx, 24f * sy)
                    quadraticTo(100f * sx, 8f * sy, 155f * sx, 34f * sy)
                }
                drawPath(arcPath, Color.White, style = Stroke(2f * sx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))))
                drawLabel(labels.getOrElse(0) { "" }, 160f, 103f, 11f, SacredGoldBright, true)
            }
            StepIllustrationKey.MADINAH -> {
                drawRect(Color(0xFFEFECE2), Offset(70f * sx, 52f * sy), Size(180f * sx, 36f * sy))
                drawArc(Color(0xFF1E8449), 180f, 180f, true, Offset(135f * sx, 27f * sy), Size(50f * sx, 50f * sy))
                drawRect(Color(0xFFEAE5D8), Offset(52f * sx, 22f * sy), Size(10f * sx, 66f * sy))
                drawRect(Color(0xFFEAE5D8), Offset(258f * sx, 22f * sy), Size(10f * sx, 66f * sy))
                drawLabel(labels.getOrElse(0) { "" }, 160f, 104f, 11f, SacredGoldBright, true)
            }
        }
        }
    }
}
