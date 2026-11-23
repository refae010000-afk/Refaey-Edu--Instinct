package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.GlobalStrings
import com.example.data.PilgrimContent
import com.example.model.ActiveModal
import com.example.model.MoreTextTab
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.DuaQuoteBox
import com.example.ui.components.MetricMiniCard
import com.example.ui.components.PilgrimBrandEmblem
import com.example.ui.components.PilgrimCard
import com.example.ui.components.PilgrimChip
import com.example.ui.components.PilgrimOutlineButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DeepInk
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldDark
import com.example.ui.theme.StatusSuccess
import com.example.util.ActivityCalorieCalculator
import java.util.Locale

private enum class MoreSectionFilter {
    ALL, ACTIVITY, DUAS, BAG, SETTINGS
}

@Composable
fun MoreAndTripScreen(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val strings = GlobalStrings.forLang(state.language)
    val clipboard = LocalClipboardManager.current
    var activeFilter by remember { mutableStateOf(MoreSectionFilter.ALL) }

    val moreItems = PilgrimContent.moreData[state.moreTextTab].orEmpty().filter {
        val q = state.moreSearchQuery.trim()
        q.isEmpty() || it.title.contains(q) || it.text.contains(q)
    }

    val chkDoneCount = PilgrimContent.checklistItems.count { state.checklistMap[it.id] ?: it.defaultChecked }
    val chkProgress = if (PilgrimContent.checklistItems.isEmpty()) 0f
    else chkDoneCount.toFloat() / PilgrimContent.checklistItems.size

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
            // 1. Smart Pilgrim ID Banner with Masjid Nabawi backdrop
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("smart_id_banner")
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.openModal(ActiveModal.PROFILE) },
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.55f))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_madinah_nabawi),
                        contentDescription = strings.smartCardHint,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        DeepInk.copy(alpha = 0.88f),
                                        EmeraldPrimary.copy(alpha = 0.90f)
                                    )
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = strings.smartCardHint,
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = state.profile.name,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${strings.mutawwifPrefix} ${state.profile.mutawwifPhone} • ${strings.bloodPrefix} ${state.profile.bloodType}",
                                color = SacredGold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Surface(
                            onClick = { viewModel.openModal(ActiveModal.ID_CARD) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("open_medical_id_btn"),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = strings.medicalIdBtn,
                                color = Color.Black,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }

            // 2. Clean Sub-Section Focus Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    MoreSectionFilter.ALL to strings.filterAll,
                    MoreSectionFilter.ACTIVITY to strings.filterActivity,
                    MoreSectionFilter.DUAS to strings.filterDuas,
                    MoreSectionFilter.BAG to strings.filterBag,
                    MoreSectionFilter.SETTINGS to strings.filterSettings
                ).forEach { (filter, label) ->
                    PilgrimChip(
                        label = label,
                        selected = activeFilter == filter,
                        onClick = { activeFilter = filter },
                        tag = "more_filter_${filter.name.lowercase()}"
                    )
                }
            }

            // 3. Ritual Calories Burned & Walking Stats Card
            if (activeFilter == MoreSectionFilter.ALL || activeFilter == MoreSectionFilter.ACTIVITY) {
                val activityStats = ActivityCalorieCalculator.calculate(
                    tawafRounds = state.tawafTotalRounds,
                    saiRounds = state.saiTotalRounds,
                    totalKm = state.totalKm,
                    weightKg = state.weightKg
                )

                PilgrimCard(accentStartBar = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader(
                            title = strings.caloriesTitle,
                            icon = Icons.Outlined.LocalFireDepartment,
                            modifier = Modifier.weight(1f)
                        )
                        PilgrimOutlineButton(
                            text = strings.resetZeroBtn,
                            onClick = { viewModel.resetActivityStats() },
                            tag = "reset_calories_btn"
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricMiniCard(
                            icon = Icons.Outlined.LocalFireDepartment,
                            label = strings.caloriesBurnedLabel,
                            value = "${activityStats.totalCalories} ${strings.caloriesUnit}",
                            highlightColor = SacredGoldDark,
                            modifier = Modifier.weight(1f)
                        )
                        MetricMiniCard(
                            icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                            label = strings.totalDistLabel,
                            value = "${String.format(Locale.US, "%.2f", activityStats.totalDistanceKm)} ${strings.kmUnit}",
                            highlightColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricMiniCard(
                            icon = Icons.Outlined.WaterDrop,
                            label = strings.estStepsLabel,
                            value = "${activityStats.estimatedSteps}",
                            highlightColor = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RitualBreakdownCard(
                            title = strings.tawafLabel,
                            roundsText = "${strings.roundsLabel} ${activityStats.tawafRounds} ${strings.roundUnit}",
                            distanceText = "${strings.distanceLabel} ${String.format(Locale.US, "%.2f", activityStats.tawafDistanceKm)} ${strings.kmUnit}",
                            caloriesText = "${strings.burnedLabel} ${activityStats.tawafCalories} ${strings.caloriesUnit}",
                            modifier = Modifier.weight(1f)
                        )
                        RitualBreakdownCard(
                            title = strings.saiLabel,
                            roundsText = "${strings.roundsLabel} ${activityStats.saiRounds} ${strings.roundUnit}",
                            distanceText = "${strings.distanceLabel} ${String.format(Locale.US, "%.2f", activityStats.saiDistanceKm)} ${strings.kmUnit}",
                            caloriesText = "${strings.burnedLabel} ${activityStats.saiCalories} ${strings.caloriesUnit}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${strings.weightLabel} ${state.weightKg} ${strings.kgUnit}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = strings.weightHint,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PilgrimOutlineButton(
                                text = "-5",
                                onClick = { viewModel.adjustWeightKg(-5) },
                                tag = "weight_dec_btn"
                            )
                            PilgrimOutlineButton(
                                text = "+5",
                                onClick = { viewModel.adjustWeightKg(5) },
                                tag = "weight_inc_btn"
                            )
                        }
                    }
                }
            }

            // 4. Duas, Short Quran & Adhkar
            if (activeFilter == MoreSectionFilter.ALL || activeFilter == MoreSectionFilter.DUAS) {
                PilgrimCard {
                    SectionHeader(
                        title = strings.duasCardTitle,
                        icon = Icons.AutoMirrored.Outlined.MenuBook
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MoreTextTab.entries.forEach { tab ->
                            PilgrimChip(
                                label = strings.moreTabs[tab] ?: tab.label,
                                selected = state.moreTextTab == tab,
                                onClick = { viewModel.setMoreTextTab(tab) },
                                tag = "more_tab_${tab.name.lowercase()}"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.moreSearchQuery,
                        onValueChange = { viewModel.setMoreSearchQuery(it) },
                        placeholder = { Text(strings.searchPlaceholder) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("more_search_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        moreItems.forEachIndexed { idx, entry ->
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = entry.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        PilgrimOutlineButton(
                                            text = "📋",
                                            onClick = {
                                                clipboard.setText(AnnotatedString("${entry.title}\n${entry.text}"))
                                                viewModel.showToast(strings.copyToast)
                                            },
                                            tag = "copy_more_$idx"
                                        )
                                        PilgrimOutlineButton(
                                            text = "🔊",
                                            onClick = { viewModel.speak(entry.text) },
                                            tag = "speak_more_$idx"
                                        )
                                    }
                                }
                                DuaQuoteBox(text = entry.text, useArabicSerif = true)
                                if (idx < moreItems.lastIndex) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }
                }
            }

            // 5. Pilgrim Bag Checklist with Animated Readiness Bar
            if (activeFilter == MoreSectionFilter.ALL || activeFilter == MoreSectionFilter.BAG) {
                PilgrimCard {
                    SectionHeader(
                        title = strings.bagChecklistTitle,
                        icon = Icons.Outlined.Backpack,
                        subtitle = "${strings.packingProgressLabel}: $chkDoneCount/${PilgrimContent.checklistItems.size}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { chkProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = StatusSuccess,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PilgrimContent.checklistItems.forEachIndexed { idx, item ->
                        val checked = state.checklistMap[item.id] ?: item.defaultChecked
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("checklist_row_${item.id}")
                                .clickable { viewModel.toggleChecklist(item.id, !checked) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { viewModel.toggleChecklist(item.id, it) },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (idx < PilgrimContent.checklistItems.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // 6. Accessibility, Display Settings & About
            if (activeFilter == MoreSectionFilter.ALL || activeFilter == MoreSectionFilter.SETTINGS) {
                PilgrimCard {
                    SectionHeader(
                        title = strings.settingsTitle,
                        icon = Icons.Outlined.Settings
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FormatSize,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = strings.elderModeLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Switch(
                            checked = state.elderMode,
                            onCheckedChange = { viewModel.toggleElderMode(it) },
                            modifier = Modifier.testTag("switch_elder_mode")
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = strings.darkModeLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Switch(
                            checked = state.darkMode,
                            onCheckedChange = { viewModel.toggleDarkMode(it) },
                            modifier = Modifier.testTag("switch_dark_mode")
                        )
                    }
                }

                PilgrimCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PilgrimBrandEmblem(modifier = Modifier.size(56.dp))
                        Text(
                            text = "${strings.appTitle} - ${strings.splashBadge}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${strings.splashDevOrg} - ${strings.splashDevName}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RitualBreakdownCard(
    title: String,
    roundsText: String,
    distanceText: String,
    caloriesText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = roundsText, style = MaterialTheme.typography.bodySmall)
            Text(text = distanceText, style = MaterialTheme.typography.bodySmall)
            Text(
                text = caloriesText,
                style = MaterialTheme.typography.labelLarge,
                color = SacredGoldDark
            )
        }
    }
}
