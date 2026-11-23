package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.AccessibleForward
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.R
import com.example.data.GlobalStrings
import com.example.model.Scene3DType
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.OfflineSacredMapCard
import com.example.ui.components.PilgrimCard
import com.example.ui.components.QiblaCompassCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright

@Composable
fun MapAndQiblaScreen(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val strings = GlobalStrings.forLang(state.language)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            viewModel.onQiblaScreenResumed()
        }
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.onQiblaScreenResumed()
                Lifecycle.Event.ON_PAUSE -> viewModel.stopCompassListening()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.stopCompassListening()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Live Real-Time Qibla Compass (Magnetometer + Accelerometer)
            QiblaCompassCard(
                strings = strings,
                language = state.language,
                compassReading = state.compassReading,
                qiblaActive = state.qiblaActive,
                qiblaBearingDeg = state.qiblaBearingDeg,
                qiblaDistanceMeters = state.qiblaDistanceMeters,
                locationSource = state.qiblaLocationSource,
                selectedCity = state.selectedQiblaCity,
                onActivateGps = { viewModel.activateQiblaCompass() },
                onSelectReferenceCity = { viewModel.selectQiblaReferenceCity(it) }
            )

            // 2. Interactive Offline Sacred Landmarks Map (Zero-Internet Local Map)
            OfflineSacredMapCard(
                language = state.language,
                darkMode = state.darkMode,
                userLat = state.qiblaLat,
                userLng = state.qiblaLng,
                hotelLat = state.hotelLat,
                hotelLng = state.hotelLng,
                onJumpTo3D = { viewModel.jumpTo3D(it) },
                onSpeak = { text, langCode -> viewModel.speak(text, langCode) }
            )

            // 3. Photographic Holy Sites Visual Map with Mashaaer Sequence
            PilgrimCard {
                SectionHeader(
                    title = strings.mapTitle,
                    icon = Icons.Outlined.Map,
                    subtitle = strings.trainLabel
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MapPhotoCard(
                            stepNum = "1",
                            imageRes = R.drawable.img_kaaba_haram,
                            label = strings.siteHaram,
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.jumpTo3D(Scene3DType.KAABA)
                        }
                        MapPhotoCard(
                            stepNum = "2",
                            imageRes = R.drawable.img_mina_jamarat,
                            label = strings.siteMina,
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.jumpTo3D(Scene3DType.JAMARAT)
                        }
                    }

                    // Railway Route Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = strings.trainLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MapPhotoCard(
                            stepNum = "3",
                            imageRes = R.drawable.img_safa_marwa,
                            label = strings.siteMuzdalifah,
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.showToast(strings.siteMuzdalifah)
                        }
                        MapPhotoCard(
                            stepNum = "4",
                            imageRes = R.drawable.img_arafat_mount,
                            label = strings.siteArafat,
                            modifier = Modifier.weight(1f)
                        ) {
                            viewModel.jumpTo3D(Scene3DType.ARAFAT)
                        }
                    }
                }
            }

            // 3. Transport & Haram Services Guide
            PilgrimCard {
                SectionHeader(
                    title = strings.transportTitle,
                    icon = Icons.Outlined.Train
                )
                Spacer(modifier = Modifier.height(10.dp))
                TransportInfoRow(Icons.Outlined.Train, strings.transportTrain)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                TransportInfoRow(Icons.AutoMirrored.Outlined.AccessibleForward, strings.transportCarts)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                TransportInfoRow(Icons.Outlined.ContentCut, strings.transportBarber)
            }
        }
    }
}

@Composable
private fun MapPhotoCard(
    stepNum: String,
    imageRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(98.dp)
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.55f)),
        shadowElevation = 3.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.80f))
                        )
                    )
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                shape = CircleShape,
                color = SacredGoldBright
            ) {
                Text(
                    text = stepNum,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun TransportInfoRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            modifier = Modifier.size(34.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}
