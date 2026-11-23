package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GlobalStrings
import com.example.data.LocalizedRitualGuideData
import com.example.data.PilgrimContent
import com.example.model.ActiveModal
import com.example.model.AppLanguage
import com.example.model.CounterMode
import com.example.model.NusukType
import com.example.model.TawafFloor
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.DuaQuoteBox
import com.example.ui.components.InteractiveRitualGuideCard
import com.example.ui.components.PilgrimCard
import com.example.ui.components.PilgrimChip
import com.example.ui.components.PilgrimGoldButton
import com.example.ui.components.PilgrimOutlineButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.SacredGold
import com.example.ui.theme.StatusSuccess
import kotlin.math.roundToInt

private enum class RitualSectionFilter {
    ALL, COUNTER, GUIDE, MIQAT_TASBIH
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RitualsScreen(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val strings = GlobalStrings.forLang(state.language)
    var showFloorMenu by remember { mutableStateOf(false) }
    var activeFilter by remember { mutableStateOf(RitualSectionFilter.ALL) }

    val displayRound = if (state.counterMode == CounterMode.JAMARAT) {
        state.jamCounts[state.jamIdx]
    } else {
        state.round
    }
    val roundDua = LocalizedRitualGuideData.roundDuasFor(state.language, state.counterMode)
        .getOrNull((displayRound - 1).coerceIn(0, 6))
        .orEmpty()

    val steps = LocalizedRitualGuideData.ritualStepsFor(state.language, state.selectedNusuk)

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
            // 1. Nusuk Type Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NusukType.entries.forEach { n ->
                    PilgrimChip(
                        label = strings.nusukChips[n] ?: n.label,
                        selected = state.selectedNusuk == n,
                        onClick = { viewModel.setNusukType(n) },
                        tag = "nusuk_chip_${n.id}"
                    )
                }
            }

            // 2. Quick Sub-Section Filter Bar for Decluttered Focus
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    RitualSectionFilter.ALL to strings.filterAll,
                    RitualSectionFilter.COUNTER to strings.filterCounter,
                    RitualSectionFilter.GUIDE to strings.filterGuide,
                    RitualSectionFilter.MIQAT_TASBIH to strings.filterMiqatTasbih
                ).forEach { (filter, label) ->
                    PilgrimChip(
                        label = label,
                        selected = activeFilter == filter,
                        onClick = { activeFilter = filter },
                        tag = "ritual_filter_${filter.name.lowercase()}"
                    )
                }
            }

            // 3. Smart Counter Card (Tawaf / Sa'i / Jamarat)
            if (activeFilter == RitualSectionFilter.ALL || activeFilter == RitualSectionFilter.COUNTER) {
                PilgrimCard(accentStartBar = true) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            CounterMode.entries.forEach { mode ->
                                PilgrimChip(
                                    label = strings.counterModes[mode] ?: mode.label,
                                    selected = state.counterMode == mode,
                                    onClick = { viewModel.setCounterMode(mode) },
                                    tag = "counter_mode_${mode.id}"
                                )
                            }
                        }

                        // Tawaf floor ETA selector
                        if (state.counterMode == CounterMode.TAWAF) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .minimumInteractiveComponentSize()
                                            .testTag("tawaf_floor_dropdown")
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { showFloorMenu = true },
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                    ) {
                                        Text(
                                            text = "${LocalizedRitualGuideData.tawafFloorLabelFor(state.language, state.tawafFloor)} ▾",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = showFloorMenu,
                                        onDismissRequest = { showFloorMenu = false }
                                    ) {
                                        TawafFloor.entries.forEach { floor ->
                                            DropdownMenuItem(
                                                text = { Text(LocalizedRitualGuideData.tawafFloorLabelFor(state.language, floor)) },
                                                onClick = {
                                                    showFloorMenu = false
                                                    viewModel.setTawafFloor(floor)
                                                }
                                            )
                                        }
                                    }
                                }
                                val remMin = ((state.tawafFloor.minutes * (7 - state.round + 1)) / 7.0).roundToInt().coerceAtLeast(0)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Schedule,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = "${strings.tawafEtaPrefix} ~$remMin ${strings.minutesUnit}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        // 3 Jamarat Selector
                        if (state.counterMode == CounterMode.JAMARAT) {
                            val jNames = LocalizedRitualGuideData.jamaratNamesFor(state.language)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                jNames.forEachIndexed { idx, name ->
                                    val count = state.jamCounts[idx]
                                    val isActive = state.jamIdx == idx
                                    val isDone = count >= 7
                                    val borderColor = when {
                                        isDone -> StatusSuccess
                                        isActive -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.outline
                                    }
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .minimumInteractiveComponentSize()
                                            .testTag("jamrah_box_$idx")
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { viewModel.selectJamrah(idx) },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.background,
                                        border = BorderStroke(1.5.dp, borderColor)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = name,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDone) StatusSuccess else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "$count/7",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = if (isDone) StatusSuccess else MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        val animatedSweep by animateFloatAsState(
                            targetValue = (displayRound / 7f) * 360f,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = "counter_sweep"
                        )

                        // Interactive Circular Counter Ring
                        Surface(
                            modifier = Modifier
                                .size(172.dp)
                                .clip(CircleShape)
                                .testTag("tawaf_counter_ring")
                                .clickable { viewModel.incrementCounter() },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.background,
                            shadowElevation = 4.dp,
                            border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.35f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                                    drawArc(
                                        color = trackColor,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                                    )
                                    drawArc(
                                        color = SacredGold,
                                        startAngle = -90f,
                                        sweepAngle = animatedSweep,
                                        useCenter = false,
                                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "$displayRound",
                                            fontSize = 40.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "/7",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = if (state.counterMode == CounterMode.JAMARAT) strings.tapPerPebble else strings.tapPerRound,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = state.counterMode == CounterMode.TAWAF && state.round == 7) {
                            PilgrimGoldButton(
                                text = strings.tawafDoneToSaiBtn,
                                onClick = {
                                    viewModel.setCounterMode(CounterMode.SAI)
                                    viewModel.resetCounter()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                tag = "switch_to_sai_btn"
                            )
                        }

                        // Counter Action Row
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PilgrimOutlineButton(
                                text = strings.resetBtn,
                                onClick = { viewModel.resetCounter() },
                                tag = "counter_reset_btn"
                            )
                            PilgrimOutlineButton(
                                text = "-1",
                                onClick = { viewModel.decrementCounter() },
                                tag = "counter_dec_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.doubtBtn,
                                onClick = { viewModel.showToast(strings.doubtToast) },
                                tag = "counter_doubt_btn"
                            )
                            PilgrimGoldButton(
                                text = strings.listenBtn,
                                onClick = { viewModel.speakCurrentRound() },
                                tag = "counter_speak_btn",
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        DuaQuoteBox(
                            text = roundDua,
                            useArabicSerif = state.language == AppLanguage.AR || state.language == AppLanguage.UR
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PilgrimOutlineButton(
                                text = strings.talbiyahBtn,
                                onClick = { viewModel.speak(PilgrimContent.TALBIYAH_TEXT, "ar") },
                                modifier = Modifier.weight(1f),
                                tag = "speak_talbiyah_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.takbeeratBtn,
                                onClick = { viewModel.speak(PilgrimContent.TAKBEERAT_TEXT, "ar") },
                                modifier = Modifier.weight(1f),
                                tag = "speak_takbeerat_btn"
                            )
                        }
                    }
                }
            }

            // 4. Quick Fiqh Strip + Illustrated Step-by-Step Guide
            if (activeFilter == RitualSectionFilter.ALL || activeFilter == RitualSectionFilter.GUIDE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PilgrimOutlineButton(
                        text = strings.ihramBtn,
                        onClick = { viewModel.openModal(ActiveModal.IHRAM) },
                        modifier = Modifier.weight(1f),
                        tag = "btn_ihram_rules"
                    )
                    PilgrimOutlineButton(
                        text = strings.mistakesBtn,
                        onClick = { viewModel.openModal(ActiveModal.MISTAKES) },
                        modifier = Modifier.weight(1f),
                        tag = "btn_common_mistakes"
                    )
                    PilgrimOutlineButton(
                        text = strings.womenBtn,
                        onClick = { viewModel.openModal(ActiveModal.WOMEN) },
                        modifier = Modifier.weight(1f),
                        tag = "btn_women_rules"
                    )
                }

                InteractiveRitualGuideCard(
                    state = state,
                    steps = steps,
                    strings = strings,
                    viewModel = viewModel
                )
            }

            // 5. Nearest Miqat GPS & Digital Tasbih Cards
            if (activeFilter == RitualSectionFilter.ALL || activeFilter == RitualSectionFilter.MIQAT_TASBIH) {
                PilgrimCard {
                    SectionHeader(
                        title = strings.miqatTitle,
                        icon = Icons.Outlined.LocationOn
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (state.nearestMiqatText.startsWith("اضغط")) strings.miqatPrompt else state.nearestMiqatText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PilgrimOutlineButton(
                        text = strings.gpsBtn,
                        onClick = { viewModel.findNearestMiqat() },
                        modifier = Modifier.fillMaxWidth(),
                        tag = "find_miqat_btn"
                    )
                }

                PilgrimCard(accentStartBar = true) {
                    SectionHeader(
                        title = strings.tasbihTitle,
                        icon = Icons.Outlined.AutoAwesome
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = strings.totalLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${state.tasbih}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PilgrimOutlineButton(
                                text = strings.resetZeroBtn,
                                onClick = { viewModel.resetTasbih() },
                                tag = "tasbih_reset_btn"
                            )
                            PilgrimGoldButton(
                                text = strings.tasbihPlusBtn,
                                onClick = { viewModel.incrementTasbih() },
                                tag = "tasbih_inc_btn"
                            )
                        }
                    }
                }
            }
        }
    }
}
