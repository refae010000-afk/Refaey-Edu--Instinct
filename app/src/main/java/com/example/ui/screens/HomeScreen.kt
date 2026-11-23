package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Sos
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GlobalStrings
import com.example.model.ActiveModal
import com.example.model.AppTab
import com.example.model.HeatDangerLevel
import com.example.model.PrayerCity
import com.example.model.Scene3DType
import com.example.model.WeatherCity
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.MetricMiniCard
import com.example.ui.components.PilgrimCard
import com.example.ui.components.PilgrimChip
import com.example.ui.components.PilgrimGoldButton
import com.example.ui.components.PilgrimOutlineButton
import com.example.ui.components.QuickToolTile
import com.example.ui.components.SectionHeader
import com.example.ui.components.holySitePhotoRes
import com.example.ui.components.weatherCityPhotoRes
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldBright
import com.example.ui.theme.SacredGoldDark
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningText
import com.example.util.ActivityCalorieCalculator
import com.example.util.HijriDateHelper
import java.util.Locale

private data class QuickTileSpec(
    val icon: ImageVector,
    val label: String,
    val tint: Color,
    val modal: ActiveModal
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = GlobalStrings.forLang(state.language)
    var showCityMenu by remember { mutableStateOf(false) }

    val heroPhotoRes = when (state.prayerCity) {
        PrayerCity.MADINAH -> R.drawable.img_madinah_nabawi
        else -> R.drawable.img_kaaba_haram
    }

    val prayerNameMapping = listOf("الفجر", "الشروق", "الظهر", "العصر", "المغرب", "العشاء")
    fun localizePrayerName(arName: String): String {
        val idx = prayerNameMapping.indexOf(arName)
        return if (idx in strings.prayerNames.indices) strings.prayerNames[idx] else arName
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Prayer Card with Sacred Photo Backdrop
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 6.dp,
                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.6f))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = heroPhotoRes),
                        contentDescription = strings.appTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        EmeraldLight.copy(alpha = 0.80f),
                                        EmeraldDark.copy(alpha = 0.94f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Top metadata row: City selector + Hijri pill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Surface(
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("prayer_city_selector")
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showCityMenu = true },
                                    color = Color.White.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = (strings.prayerCities[state.prayerCity] ?: state.prayerCity.label) + " ▾",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showCityMenu,
                                    onDismissRequest = { showCityMenu = false }
                                ) {
                                    PrayerCity.entries.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(strings.prayerCities[city] ?: city.label) },
                                            onClick = {
                                                showCityMenu = false
                                                viewModel.selectPrayerCity(city)
                                            }
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Black.copy(alpha = 0.28f),
                                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = HijriDateHelper.todayHijriFormatted(),
                                    color = SacredGoldBright,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        // Next prayer highlight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "${strings.nextPrayerPrefix} ${localizePrayerName(state.prayerSchedule.nextName)}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.14f)
                                ) {
                                    Text(
                                        text = "${strings.remainingPrefix} ${state.prayerSchedule.countdownFormatted}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = state.prayerSchedule.nextTimeFormatted,
                                    color = SacredGoldBright,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = if (state.showAllPrayers) strings.togglePrayersHide else strings.togglePrayersShow,
                                    color = SacredGold,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("toggle_all_prayers")
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.toggleAllPrayers() }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = state.showAllPrayers) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    maxItemsInEachRow = 3,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    state.prayerSchedule.allPrayers.forEach { entry ->
                                        val isNext = entry.name == state.prayerSchedule.nextName
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(
                                                    if (isNext) SacredGold.copy(alpha = 0.25f)
                                                    else Color.White.copy(alpha = 0.12f)
                                                )
                                                .then(
                                                    if (isNext) Modifier.border(1.dp, SacredGoldBright, RoundedCornerShape(10.dp))
                                                    else Modifier
                                                )
                                                .padding(vertical = 7.dp, horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = localizePrayerName(entry.name),
                                                color = if (isNext) SacredGoldBright else Color.White.copy(alpha = 0.85f),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                            Text(
                                                text = entry.formattedTime,
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${strings.lastThirdNight} ${state.prayerSchedule.lastThirdFormatted}",
                                    color = SacredGoldBright,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        PilgrimGoldButton(
                            text = strings.heroStartBtn,
                            onClick = { viewModel.selectTab(AppTab.RITUALS) },
                            modifier = Modifier.fillMaxWidth(),
                            tag = "hero_start_rituals_btn"
                        )
                    }
                }
            }

            // 2. Holy Sanctuaries Visual Photo Gallery
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader(
                    title = strings.holyGalleryTitle,
                    icon = Icons.Outlined.PhotoLibrary,
                    actionLabel = strings.nav3D,
                    onAction = { viewModel.selectTab(AppTab.TOUR_3D) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Scene3DType.entries.forEach { scene ->
                        Surface(
                            modifier = Modifier
                                .width(150.dp)
                                .height(94.dp)
                                .testTag("home_gallery_${scene.id}")
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { viewModel.jumpTo3D(scene) },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.55f)),
                            shadowElevation = 3.dp
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = holySitePhotoRes(scene)),
                                    contentDescription = strings.sceneChips[scene] ?: scene.chipLabel,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f))
                                            )
                                        )
                                )
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strings.sceneChips[scene] ?: scene.chipLabel,
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = null,
                                        tint = SacredGoldBright,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Quick 8-Tile Toolkit with M3 Iconography
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader(
                    title = strings.quickToolsSectionTitle,
                    icon = Icons.Outlined.GridView
                )
                val quickItems = listOf(
                    QuickTileSpec(Icons.Outlined.Inventory2, strings.tileShoe, EmeraldPrimary, ActiveModal.SHOE),
                    QuickTileSpec(Icons.Outlined.Hotel, strings.tileHotel, EmeraldPrimary, ActiveModal.HOTEL),
                    QuickTileSpec(Icons.Outlined.Psychology, strings.tileMufti, SacredGoldDark, ActiveModal.AI_FAQ),
                    QuickTileSpec(Icons.AutoMirrored.Outlined.MenuBook, strings.tileFidyah, SacredGoldDark, ActiveModal.FIDYAH),
                    QuickTileSpec(Icons.Outlined.FavoriteBorder, strings.tileLoved, StatusSuccess, ActiveModal.LOVED_ONES),
                    QuickTileSpec(Icons.Outlined.Translate, strings.tileTranslator, EmeraldPrimary, ActiveModal.TRANSLATOR),
                    QuickTileSpec(Icons.Outlined.CurrencyExchange, strings.tileCurrency, SacredGoldDark, ActiveModal.CURRENCY),
                    QuickTileSpec(Icons.Outlined.Sos, strings.tileSos, StatusDanger, ActiveModal.SOS)
                )
                quickItems.chunked(4).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { spec ->
                            QuickToolTile(
                                icon = spec.icon,
                                label = spec.label,
                                tintColor = spec.tint,
                                tag = "quick_tile_${spec.modal.name.lowercase()}",
                                onClick = { viewModel.openModal(spec.modal) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 4. Live Weather & Coordinate-Based Heat Push Alerts Card
            PilgrimCard {
                val dangerStyle = when (state.weatherState.dangerLevel) {
                    HeatDangerLevel.EXTREME -> Triple(strings.heatExtremeLabel, StatusDangerContainer, StatusDanger)
                    HeatDangerLevel.DANGER -> Triple(strings.heatDangerLabel, StatusDangerContainer, StatusDanger)
                    HeatDangerLevel.CAUTION -> Triple(strings.heatCautionLabel, StatusWarningContainer, StatusWarningText)
                    HeatDangerLevel.SAFE -> Triple(strings.heatSafeLabel, StatusSuccessContainer, StatusSuccess)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader(
                        title = strings.weatherTitle,
                        icon = Icons.Outlined.WbSunny,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = dangerStyle.second
                    ) {
                        Text(
                            text = dangerStyle.first,
                            color = dangerStyle.third,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WeatherCity.entries.forEach { wCity ->
                        PilgrimChip(
                            label = strings.weatherCities[wCity] ?: wCity.shortLabel,
                            selected = state.weatherState.city == wCity,
                            onClick = { viewModel.loadWeather(wCity) },
                            tag = "weather_chip_${wCity.id}"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val localizedCityName = strings.weatherCities[state.weatherState.city] ?: state.weatherState.city.fullName
                val localizedCond = when (state.weatherState.iconEmoji) {
                    "☀️" -> strings.weatherClear
                    "⛅" -> strings.weatherCloudy
                    else -> strings.weatherRain
                }
                val localizedAdvice = if (state.weatherState.temperatureC >= 34) {
                    strings.weatherAdviceHot
                } else {
                    strings.weatherAdviceGood
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = weatherCityPhotoRes(state.weatherState.city)),
                        contentDescription = localizedCityName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = if (state.weatherState.isLoading) strings.weatherUpdating else "$localizedCityName: $localizedCond",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${strings.heatFeelsLikePrefix} ${state.weatherState.feelsLikeC}°C • $localizedAdvice",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.weatherState.iconEmoji, fontSize = 24.sp)
                        Text(
                            text = "${state.weatherState.temperatureC}°C",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (state.weatherState.dangerLevel >= HeatDangerLevel.DANGER) StatusDanger else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniCard(
                        label = strings.weatherHighLow,
                        value = "${state.weatherState.highC}° / ${state.weatherState.lowC}°",
                        highlightColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        label = strings.weatherHumidity,
                        value = "${state.weatherState.humidity}%",
                        highlightColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        label = strings.weatherWindUv,
                        value = "${state.weatherState.windKmh} • UV ${state.weatherState.uvIndex}",
                        highlightColor = SacredGoldDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Heat Danger GPS Push Alerts Strip
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (state.weatherAlertsEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.NotificationsActive,
                                    contentDescription = null,
                                    tint = if (state.weatherAlertsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = strings.heatPushToggleTitle,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = strings.heatPushToggleDesc,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Switch(
                                checked = state.weatherAlertsEnabled,
                                onCheckedChange = { viewModel.toggleWeatherAlerts(it) },
                                modifier = Modifier.testTag("switch_heat_alerts")
                            )
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${strings.heatThresholdLabel} ≥ ${state.weatherDangerThresholdC}°C",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Row(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PilgrimOutlineButton(
                                    text = "-2°",
                                    onClick = { viewModel.adjustWeatherDangerThreshold(-2) },
                                    tag = "threshold_dec_btn"
                                )
                                PilgrimOutlineButton(
                                    text = "+2°",
                                    onClick = { viewModel.adjustWeatherDangerThreshold(2) },
                                    tag = "threshold_inc_btn"
                                )
                                PilgrimOutlineButton(
                                    text = strings.heatTestAlertBtn,
                                    onClick = { viewModel.sendTestHeatAlertNotification() },
                                    tag = "test_heat_push_btn"
                                )
                            }
                        }
                    }
                }
            }

            // 5. Health, Activity & Emergency Quick Card
            val homeActivity = remember(state.tawafTotalRounds, state.saiTotalRounds, state.totalKm, state.weightKg) {
                ActivityCalorieCalculator.calculate(
                    tawafRounds = state.tawafTotalRounds,
                    saiRounds = state.saiTotalRounds,
                    totalKm = state.totalKm,
                    weightKg = state.weightKg
                )
            }
            PilgrimCard(accentStartBar = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.VolunteerActivism,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = strings.healthTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    PilgrimOutlineButton(
                        text = if (state.waterReminderActive) strings.waterOn else strings.waterOff,
                        onClick = { viewModel.toggleWaterReminder() },
                        tag = "water_reminder_btn"
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniCard(
                        icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                        label = strings.totalDistLabel,
                        value = "${String.format(Locale.US, "%.2f", homeActivity.totalDistanceKm)} ${strings.kmUnit}",
                        highlightColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        icon = Icons.Outlined.LocalFireDepartment,
                        label = strings.caloriesBurnedLabel,
                        value = "${homeActivity.totalCalories} ${strings.caloriesShort}",
                        highlightColor = SacredGoldDark,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        icon = Icons.Outlined.WaterDrop,
                        label = strings.estStepsLabel,
                        value = "${homeActivity.estimatedSteps}",
                        highlightColor = StatusSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PilgrimOutlineButton(
                        text = strings.shareLocationBtn,
                        onClick = { viewModel.shareMyLocation() },
                        modifier = Modifier.weight(1f),
                        tag = "share_location_btn"
                    )
                    PilgrimOutlineButton(
                        text = strings.detailsBtn,
                        onClick = { viewModel.selectTab(AppTab.MORE) },
                        tag = "home_calories_details_btn"
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PilgrimOutlineButton(
                        text = "🏥 937",
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:937")))
                        },
                        modifier = Modifier.weight(1f),
                        tag = "dial_937_btn"
                    )
                    PilgrimOutlineButton(
                        text = "🚑 997",
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:997")))
                        },
                        modifier = Modifier.weight(1f),
                        borderColor = StatusDanger,
                        textColor = StatusDanger,
                        tag = "dial_997_btn"
                    )
                }
            }

            // 6. Interactive Saved Trip Notes Card
            PilgrimCard {
                SectionHeader(
                    title = strings.savedTripTitle,
                    icon = Icons.Outlined.BookmarkBorder
                )
                Spacer(modifier = Modifier.height(8.dp))
                val shoeDisplay = if (state.shoeLocation == "لم يتم التحديد") strings.notSetText else state.shoeLocation
                SavedTripRow(
                    label = "${strings.savedShoePrefix} $shoeDisplay",
                    icon = Icons.Outlined.Inventory2,
                    tag = "saved_shoe_row",
                    onClick = { viewModel.openModal(ActiveModal.SHOE) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                SavedTripRow(
                    label = "${strings.savedHotelPrefix} ${state.hotelName}",
                    icon = Icons.Outlined.Hotel,
                    tag = "saved_hotel_row",
                    onClick = { viewModel.openModal(ActiveModal.HOTEL) }
                )
            }
        }
    }
}

@Composable
private fun SavedTripRow(
    label: String,
    icon: ImageVector,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .testTag(tag)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}
