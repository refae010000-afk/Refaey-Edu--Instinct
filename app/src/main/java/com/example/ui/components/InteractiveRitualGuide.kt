package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.GlobalUiStrings
import com.example.model.AppLanguage
import com.example.model.RitualBadgeType
import com.example.model.RitualStep
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.theme.SacredGold
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveRitualGuideCard(
    state: PilgrimUiState,
    steps: List<RitualStep>,
    strings: GlobalUiStrings,
    viewModel: PilgrimViewModel,
    modifier: Modifier = Modifier
) {
    val doneCount = steps.count { it.id in state.completedSteps }
    val progress = if (steps.isEmpty()) 0f else doneCount.toFloat() / steps.size
    val activeIdx = state.activeGuideStepIndex.coerceIn(0, (steps.size - 1).coerceAtLeast(0))

    PilgrimCard(modifier = modifier, accentStartBar = true) {
        // 1. Header + Step/Overview Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(
                title = strings.guideTitles[state.selectedNusuk] ?: state.selectedNusuk.guideTitle,
                icon = Icons.Outlined.Route,
                subtitle = "$doneCount / ${steps.size} ${strings.stepsLabel}",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            PilgrimChip(
                label = if (state.guideShowAllStages) strings.guideModeStepByStep else strings.guideModeOverview,
                selected = state.guideShowAllStages,
                onClick = { viewModel.toggleGuideViewMode() },
                tag = "guide_view_mode_toggle"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. DataStore-Backed 6-Language Switcher Bar for the Ritual Guide
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = strings.guideLanguageLabel,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = strings.guideLanguageLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AppLanguage.entries.forEach { lang ->
                        PilgrimChip(
                            label = lang.label,
                            selected = state.language == lang,
                            onClick = { viewModel.setLanguage(lang) },
                            tag = "guide_lang_${lang.code}"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Overall Nusuk Progress Bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = SacredGold,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
        )

        if (steps.isEmpty()) return@PilgrimCard

        Spacer(modifier = Modifier.height(12.dp))

        if (!state.guideShowAllStages) {
            // 3. Interactive Stepper Node Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { idx, stepItem ->
                    val isDone = stepItem.id in state.completedSteps
                    val isSelected = idx == activeIdx
                    val bgColor = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isDone -> StatusSuccessContainer
                        else -> MaterialTheme.colorScheme.background
                    }
                    val contentColor = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        isDone -> StatusSuccess
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    val borderColor = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isDone -> StatusSuccess
                        else -> MaterialTheme.colorScheme.outline
                    }

                    Surface(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("guide_stepper_$idx")
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.selectGuideStep(idx, steps.size) },
                        shape = RoundedCornerShape(14.dp),
                        color = bgColor,
                        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                        shadowElevation = if (isSelected) 3.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (isDone && !isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = strings.guideCompletedBadge,
                                    tint = contentColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = contentColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Active Interactive Stage Card
            val activeStep = steps[activeIdx]
            AnimatedContent(
                targetState = Triple(activeIdx, activeStep, state.language),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "active_ritual_stage"
            ) { (idx, step, lang) ->
                ActiveRitualStageView(
                    step = step,
                    stepIndex = idx,
                    totalSteps = steps.size,
                    isDone = step.id in state.completedSteps,
                    language = lang,
                    strings = strings,
                    onToggleDone = { viewModel.toggleStepDone(step.id) },
                    onPreviewImage = { viewModel.openStepPreview(step) },
                    onListen = {
                        val spokenDetails = step.detailedPoints.joinToString(". ")
                        viewModel.speak(
                            "${step.title}. ${step.description}. $spokenDetails",
                            lang.code
                        )
                    },
                    onView3D = { viewModel.jumpTo3D(step.scene3D) },
                    onPrev = { viewModel.previousGuideStep() },
                    onCompleteAndNext = {
                        viewModel.completeAndAdvanceGuideStep(step.id, steps.size)
                    }
                )
            }
        } else {
            // 5. Overview List Mode (tap any stage to jump directly into interactive view)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                steps.forEachIndexed { idx, step ->
                    val isDone = step.id in state.completedSteps
                    val isSelected = idx == activeIdx
                    val badgeColors = badgeColorsFor(step.badgeType)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("guide_overview_step_${step.id}")
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { viewModel.selectGuideStep(idx, steps.size) },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.background,
                        border = BorderStroke(
                            width = if (isSelected || isDone) 1.5.dp else 1.dp,
                            color = when {
                                isDone -> StatusSuccess
                                isSelected -> SacredGold
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isDone) StatusSuccess else MaterialTheme.colorScheme.primary
                            ) {
                                Box(
                                    modifier = Modifier.size(30.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${idx + 1}",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = step.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f, fill = false),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = badgeColors.first
                                    ) {
                                        Text(
                                            text = strings.badges[step.badgeType] ?: step.badgeType.label,
                                            color = badgeColors.second,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = step.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Checkbox(
                                checked = isDone,
                                onCheckedChange = { viewModel.toggleStepDone(step.id) },
                                colors = CheckboxDefaults.colors(checkedColor = StatusSuccess)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveRitualStageView(
    step: RitualStep,
    stepIndex: Int,
    totalSteps: Int,
    isDone: Boolean,
    language: AppLanguage,
    strings: GlobalUiStrings,
    onToggleDone: () -> Unit,
    onPreviewImage: () -> Unit,
    onListen: () -> Unit,
    onView3D: () -> Unit,
    onPrev: () -> Unit,
    onCompleteAndNext: () -> Unit
) {
    val badgeColors = badgeColorsFor(step.badgeType)
    val isLast = stepIndex >= totalSteps - 1

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_ritual_stage_${step.id}"),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(
            width = if (isDone) 1.5.dp else 1.dp,
            color = if (isDone) StatusSuccess else SacredGold.copy(alpha = 0.65f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stage Header: Counter Pill + Badge + Completion Status Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "${strings.guideStageLabel} ${stepIndex + 1} / $totalSteps",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = badgeColors.first
                    ) {
                        Text(
                            text = strings.badges[step.badgeType] ?: step.badgeType.label,
                            color = badgeColors.second,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("guide_toggle_done_${step.id}")
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onToggleDone),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDone) StatusSuccessContainer else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, if (isDone) StatusSuccess else MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isDone) StatusSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        if (isDone) {
                            Text(
                                text = strings.guideCompletedBadge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess
                            )
                        }
                    }
                }
            }

            // Title & Location Hint
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (step.locationHint.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = SacredGold,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = step.locationHint,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Visual Illustration + Zoom Hint
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(142.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f), RoundedCornerShape(14.dp))
                    .testTag("step_illus_${step.id}")
                    .clickable(onClick = onPreviewImage)
            ) {
                RitualStepIllustration(
                    key = step.illustrationKey,
                    language = language,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = strings.zoomHint,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Visual Description Box
            if (step.visualCaption.isNotBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
                ) {
                    Column(
                        modifier = Modifier.padding(11.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = strings.guideVisualDescLabel,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = step.visualCaption,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Text Description + Action Checklist
            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            if (step.detailedPoints.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(11.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ViewList,
                                contentDescription = null,
                                tint = SacredGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = strings.guideActionStepsLabel,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        step.detailedPoints.forEachIndexed { pointIdx, point ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = SacredGold.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(
                                        text = "${pointIdx + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }
                                Text(
                                    text = point,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Stage Dua
            if (step.stageDua.isNotBlank()) {
                DuaQuoteBox(
                    text = step.stageDua,
                    title = strings.guideStageDuaLabel,
                    useArabicSerif = language == AppLanguage.AR || language == AppLanguage.UR
                )
            }

            // Audio & 3D Scene Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PilgrimOutlineButton(
                    text = strings.listenBtn,
                    onClick = onListen,
                    modifier = Modifier.weight(1f),
                    tag = "speak_step_${step.id}"
                )
                PilgrimOutlineButton(
                    text = strings.view3DBtn,
                    onClick = onView3D,
                    modifier = Modifier.weight(1f),
                    tag = "jump_3d_${step.id}"
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            // Wizard Navigation Bar: Previous + Complete & Next / Finish
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (stepIndex > 0) {
                    PilgrimOutlineButton(
                        text = strings.guidePrevBtn,
                        onClick = onPrev,
                        modifier = Modifier.weight(1f),
                        tag = "guide_prev_btn"
                    )
                }
                PilgrimGoldButton(
                    text = if (isLast) strings.guideFinishBtn else strings.guideCompleteNextBtn,
                    onClick = onCompleteAndNext,
                    modifier = Modifier.weight(1.4f),
                    tag = "guide_next_btn"
                )
            }
        }
    }
}

private fun badgeColorsFor(type: RitualBadgeType): Pair<Color, Color> = when (type) {
    RitualBadgeType.RUKN -> StatusDangerContainer to StatusDanger
    RitualBadgeType.WAJIB -> StatusSuccessContainer to StatusSuccess
    RitualBadgeType.SUNNAH -> StatusWarningContainer to StatusWarningText
}
