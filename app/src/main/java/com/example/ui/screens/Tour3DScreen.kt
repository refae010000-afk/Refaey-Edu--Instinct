package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material.icons.rounded.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.GlobalStrings
import com.example.data.PilgrimContent
import com.example.model.Scene3DType
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.PilgrimCard
import com.example.ui.components.PilgrimChip
import com.example.ui.components.PilgrimOutlineButton
import com.example.ui.components.Scene3DCanvas
import com.example.ui.components.holySitePhotoRes
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tour3DScreen(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val strings = GlobalStrings.forLang(state.language)
    var yaw by remember { mutableFloatStateOf(0.55f) }
    var pitch by remember { mutableFloatStateOf(0.55f) }
    var zoom by remember { mutableFloatStateOf(1.05f) }
    var showPhotoMode by remember { mutableStateOf(false) }

    val sceneInfo = strings.scene3DInfos[state.selectedScene3D]
        ?: PilgrimContent.scene3DInfos[state.selectedScene3D]
        ?: PilgrimContent.scene3DInfos.values.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Scene Selector Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Scene3DType.entries.forEach { scene ->
                    PilgrimChip(
                        label = strings.sceneChips[scene] ?: scene.chipLabel,
                        selected = state.selectedScene3D == scene,
                        onClick = { viewModel.selectScene3D(scene) },
                        tag = "scene3d_chip_${scene.id}"
                    )
                }
            }

            // Segmented Mode Toggle (3D Model vs Real Photo) + Services Overlay Chip
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PilgrimChip(
                    label = strings.mode3DLabel,
                    selected = !showPhotoMode,
                    onClick = { showPhotoMode = false },
                    tag = "tour_mode_3d"
                )
                PilgrimChip(
                    label = strings.modePhotoLabel,
                    selected = showPhotoMode,
                    onClick = { showPhotoMode = true },
                    tag = "tour_mode_photo"
                )
                if (!showPhotoMode) {
                    PilgrimChip(
                        label = strings.servicesLayerToggleLabel,
                        selected = state.show3DServicesLayer,
                        onClick = { viewModel.toggle3DServicesLayer() },
                        tag = "toggle_3d_services"
                    )
                }
            }

            // Viewport Box with Smooth Crossfade
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.5.dp, SacredGold),
                shadowElevation = 6.dp
            ) {
                AnimatedContent(
                    targetState = showPhotoMode,
                    transitionSpec = { fadeIn(tween(240)) togetherWith fadeOut(tween(240)) },
                    label = "tour_viewport_switch"
                ) { isPhoto ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (isPhoto) {
                            Image(
                                painter = painterResource(id = holySitePhotoRes(state.selectedScene3D)),
                                contentDescription = strings.sceneChips[state.selectedScene3D] ?: sceneInfo.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Black.copy(alpha = 0.25f),
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.72f)
                                            )
                                        )
                                    )
                            )
                            Text(
                                text = strings.sceneChips[state.selectedScene3D] ?: sceneInfo.title,
                                color = SacredGoldBright,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            )
                        } else {
                            Scene3DCanvas(
                                scene = state.selectedScene3D,
                                labels = strings.scene3DLabels,
                                showServicesLayer = state.show3DServicesLayer,
                                autoOrbit = state.autoOrbit3D,
                                yaw = yaw,
                                pitch = pitch,
                                zoom = zoom,
                                onOrbitDrag = { dx, dy ->
                                    if (state.autoOrbit3D) viewModel.toggleAutoOrbit3D()
                                    yaw += dx
                                    pitch = (pitch + dy).coerceIn(0.15f, 1.38f)
                                },
                                onZoomChange = { factor ->
                                    zoom = (zoom * factor).coerceIn(0.65f, 2.2f)
                                },
                                onAutoOrbitTick = { delta -> yaw += delta }
                            )

                            // Top HUD Hints
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                HudBadge(text = strings.dragHint, textColor = Color.White)
                                if (state.selectedScene3D == Scene3DType.KAABA) {
                                    HudBadge(text = strings.ccwBadge, textColor = SacredGoldBright)
                                }
                            }

                            // Clean Vector HUD Control Bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    HudIconButton(Icons.Rounded.ZoomIn, "Zoom In", "zoom_in_3d") {
                                        zoom = (zoom + 0.18f).coerceIn(0.65f, 2.2f)
                                    }
                                    HudIconButton(Icons.Rounded.ZoomOut, "Zoom Out", "zoom_out_3d") {
                                        zoom = (zoom - 0.18f).coerceIn(0.65f, 2.2f)
                                    }
                                    HudIconButton(
                                        icon = if (pitch > 1.05f) Icons.Rounded.ViewInAr else Icons.Rounded.Layers,
                                        contentDescription = if (pitch > 1.05f) strings.viewIso else strings.viewTop,
                                        tag = "top_view_3d"
                                    ) {
                                        pitch = if (pitch > 1.05f) 0.55f else 1.32f
                                    }
                                    HudIconButton(Icons.Rounded.Refresh, "Reset View", "reset_cam_3d") {
                                        yaw = 0.55f
                                        pitch = 0.55f
                                        zoom = 1.05f
                                    }
                                }

                                Surface(
                                    onClick = { viewModel.toggleAutoOrbit3D() },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("toggle_orbit_3d"),
                                    shape = RoundedCornerShape(12.dp),
                                    color = EmeraldPrimary.copy(alpha = 0.90f),
                                    border = BorderStroke(1.dp, SacredGold)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (state.autoOrbit3D) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (state.autoOrbit3D) strings.orbitPause else strings.orbitPlay,
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Color-Coded Legend for Buses, Ambulance Assembly & Services
            AnimatedVisibility(visible = !showPhotoMode && state.show3DServicesLayer) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = strings.legendTitle3D,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            LegendItemDot(color = Color(0xFFF59E0B), label = strings.legendBusRoute)
                            LegendItemDot(color = Color(0xFFEF4444), label = strings.legendAmbulanceHub)
                            LegendItemDot(color = Color(0xFF0EA5E9), label = strings.legendWaterCooling)
                            LegendItemDot(color = Color(0xFF10B981), label = strings.legendTrainGuidance)
                        }
                    }
                }
            }

            // Scene Details Card
            PilgrimCard(accentStartBar = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sceneInfo.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )
                    PilgrimOutlineButton(
                        text = strings.speakBtn,
                        onClick = { viewModel.speak(sceneInfo.description, state.language.code) },
                        tag = "speak_3d_info_btn"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = sceneInfo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sceneInfo.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItemDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HudBadge(text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.56f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HudIconButton(
    icon: ImageVector,
    contentDescription: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag(tag),
        shape = RoundedCornerShape(12.dp),
        color = EmeraldPrimary.copy(alpha = 0.90f),
        border = BorderStroke(1.dp, SacredGold)
    ) {
        Box(
            modifier = Modifier.padding(9.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
