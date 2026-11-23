package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.GlobalStrings
import com.example.data.PilgrimContent
import com.example.model.ActiveModal
import com.example.model.PilgrimProfile
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.theme.SacredGold
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PilgrimModalHost(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel
) {
    if (state.activeModal == ActiveModal.NONE) return

    BackHandler { viewModel.closeModal() }

    val context = LocalContext.current
    val strings = GlobalStrings.forLang(state.language)

    Dialog(onDismissRequest = { viewModel.closeModal() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 590.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = if (state.activeModal == ActiveModal.ID_CARD) 2.5.dp else 1.dp,
                color = if (state.activeModal == ActiveModal.ID_CARD) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                when (state.activeModal) {
                    ActiveModal.STEP_PREVIEW -> {
                        val step = state.previewStep
                        if (step != null) {
                            ModalHeader(step.title) { viewModel.closeModal() }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(176.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                            ) {
                                RitualStepIllustration(
                                    key = step.illustrationKey,
                                    language = state.language,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                            if (step.visualCaption.isNotBlank()) {
                                DuaQuoteBox(
                                    title = strings.guideVisualDescLabel,
                                    text = step.visualCaption
                                )
                            }
                            Text(
                                text = step.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (step.detailedPoints.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = strings.guideActionStepsLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    step.detailedPoints.forEachIndexed { idx, pt ->
                                        Text(
                                            text = "${idx + 1}. $pt",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            if (step.stageDua.isNotBlank()) {
                                DuaQuoteBox(
                                    title = strings.guideStageDuaLabel,
                                    text = step.stageDua,
                                    useArabicSerif = state.language == com.example.model.AppLanguage.AR ||
                                        state.language == com.example.model.AppLanguage.UR
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PilgrimOutlineButton(
                                    text = strings.view3DBtn,
                                    onClick = {
                                        viewModel.closeModal()
                                        viewModel.jumpTo3D(step.scene3D)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                PilgrimGoldButton(
                                    text = strings.understoodBtn,
                                    onClick = { viewModel.closeModal() },
                                    modifier = Modifier.weight(1f),
                                    tag = "modal_step_ok"
                                )
                            }
                        }
                    }

                    ActiveModal.ABOUT -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PilgrimBrandEmblem(modifier = Modifier.size(76.dp))
                            Text(
                                text = strings.splashTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = strings.splashBadge,
                                style = MaterialTheme.typography.titleMedium,
                                color = SacredGold
                            )
                            Text(
                                text = strings.splashDevOrg,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = strings.splashDevName,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            PilgrimGoldButton(
                                text = strings.understoodBtn,
                                onClick = { viewModel.closeModal() },
                                modifier = Modifier.fillMaxWidth(),
                                tag = "modal_about_ok"
                            )
                        }
                    }

                    ActiveModal.CURRENCY -> {
                        var sarInput by remember { mutableStateOf("100") }
                        val sar = sarInput.toDoubleOrNull() ?: 0.0
                        ModalHeader("💱 ${strings.currencyModalTitle}") { viewModel.closeModal() }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("50", "100", "500", "1000").forEach { preset ->
                                PilgrimChip(
                                    label = preset,
                                    selected = sarInput == preset,
                                    onClick = { sarInput = preset },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        OutlinedTextField(
                            value = sarInput,
                            onValueChange = { sarInput = it },
                            label = { Text(strings.currencyAmountLabel) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("currency_sar_input")
                        )
                        DuaQuoteBox(
                            title = "$sar SAR ≈",
                            text = """
                                🇺🇸 USD: ${String.format(Locale.US, "%.2f", sar * 0.266)} $
                                🇪🇺 EUR: ${String.format(Locale.US, "%.2f", sar * 0.245)} €
                                🇬🇧 GBP: ${String.format(Locale.US, "%.2f", sar * 0.210)} £
                                🇪🇬 EGP: ${String.format(Locale.US, "%.1f", sar * 13.1)} EGP
                                🇵🇰 PKR: ${(sar * 74.2).roundToInt()} PKR
                                🇮🇩 IDR: ${(sar * 4200).roundToInt()} IDR
                                🇹🇷 TRY: ${String.format(Locale.US, "%.1f", sar * 9.2)} TRY
                                🇲🇾 MYR: ${String.format(Locale.US, "%.2f", sar * 1.18)} RM
                                🇮🇳 INR: ${String.format(Locale.US, "%.1f", sar * 22.5)} ₹
                                🇧🇩 BDT: ${(sar * 31.8).roundToInt()} ৳
                            """.trimIndent()
                        )
                    }

                    ActiveModal.MISTAKES -> {
                        ModalHeader("⚠️ ${strings.mistakesModalTitle}") { viewModel.closeModal() }
                        val mistakes = listOf(
                            "1. تجاوز الميقات دون إحرام:" to "لا يجوز تأخير الإحرام لجدة لمن قصد النسك.",
                            "2. الحجر الأسود:" to "يُشار باليد اليمنى فقط مع التكبير دون مزاحمة.",
                            "3. الطواف داخل حجر إسماعيل:" to "الحجر من الكعبة، من طاف داخله بطل شوطه.",
                            "4. حدود عرفة:" to "تأكد من الدخول داخل حدود عرفة (مقدمة نمرة خارجها).",
                            "5. التقصير:" to "يجب تعميم الرأس بالتقصير ولا يكفي قص شعرات قليلة."
                        )
                        mistakes.forEach { (t, d) -> DuaQuoteBox(title = t, text = d) }
                    }

                    ActiveModal.WOMEN -> {
                        ModalHeader("👩 ${strings.womenModalTitle}") { viewModel.closeModal() }
                        val rules = listOf(
                            "• إحرام المرأة:" to "تلبس ثيابها الساترة دون نقاب أو قفازين، وتسدل خمارها عند مرور الرجال.",
                            "• الحائض والنفساء:" to "تفعل كل المناسك عدا الطواف حتى تطهر، ويسقط عنها طواف الوداع.",
                            "• الاضطباع والرمل والهرولة:" to "خاص بالرجال فقط.",
                            "• مزدلفة للضعفة:" to "يُرخص للنساء وكبار السن الدفع في النصف الثاني من ليلة العيد."
                        )
                        rules.forEach { (t, d) -> DuaQuoteBox(title = t, text = d) }
                    }

                    ActiveModal.FIDYAH -> {
                        ModalHeader("🐑 ${strings.fidyahModalTitle}") { viewModel.closeModal() }
                        val buttons = listOf(
                            "tamattu" to "هدي التمتع والقران",
                            "wajib" to "ترك واجب من واجبات النسك",
                            "adha" to "فعل محظور (فدية الأذى)",
                            "niyabah" to "الحج أو العمرة بالنيابة"
                        )
                        buttons.forEach { (key, label) ->
                            PilgrimOutlineButton(
                                text = label,
                                onClick = { viewModel.selectFidyahRule(key) },
                                modifier = Modifier.fillMaxWidth(),
                                tag = "fidyah_btn_$key"
                            )
                        }
                        val selected = state.selectedFidyahKey?.let { PilgrimContent.fidyahRules[it] }
                        DuaQuoteBox(
                            title = selected?.first,
                            text = selected?.second ?: "اختر الحالة لعرض التفصيل الفقهي وصيغة النية."
                        )
                    }

                    ActiveModal.LOVED_ONES -> {
                        var newLoved by remember { mutableStateOf("") }
                        ModalHeader("🤲 ${strings.lovedOnesModalTitle}") { viewModel.closeModal() }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newLoved,
                                onValueChange = { newLoved = it },
                                placeholder = { Text("مثال: أمي وأبي — الشفاء") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("loved_ones_input")
                            )
                            PilgrimGoldButton(
                                text = "+",
                                onClick = {
                                    viewModel.addLovedOne(newLoved)
                                    newLoved = ""
                                },
                                tag = "add_loved_one_btn"
                            )
                        }
                        if (state.lovedOnes.isNotEmpty()) {
                            PilgrimOutlineButton(
                                text = "🔊 ${strings.listenBtn}",
                                onClick = { viewModel.speak(state.lovedOnes.joinToString(". ")) },
                                modifier = Modifier.fillMaxWidth(),
                                tag = "speak_all_loved_btn"
                            )
                        }
                        if (state.lovedOnes.isEmpty()) {
                            Text(
                                text = strings.notSetText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            state.lovedOnes.forEachIndexed { idx, entry ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🤲 $entry",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    PilgrimOutlineButton(
                                        text = "✕",
                                        onClick = { viewModel.removeLovedOne(idx) },
                                        borderColor = StatusDanger,
                                        textColor = StatusDanger
                                    )
                                }
                                if (idx < state.lovedOnes.lastIndex) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }

                    ActiveModal.AI_FAQ -> {
                        var expandedIdx by remember { mutableStateOf<Int?>(null) }
                        ModalHeader("🤖 ${strings.faqModalTitle}") { viewModel.closeModal() }
                        OutlinedTextField(
                            value = state.faqSearchQuery,
                            onValueChange = { viewModel.setFaqSearchQuery(it) },
                            placeholder = { Text(strings.searchPlaceholder) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("faq_search_input")
                        )
                        val filtered = PilgrimContent.faqList.filter {
                            val q = state.faqSearchQuery.trim()
                            q.isEmpty() || it.question.contains(q) || it.answer.contains(q)
                        }
                        filtered.forEachIndexed { idx, item ->
                            val isOpen = expandedIdx == idx
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .minimumInteractiveComponentSize()
                                    .clickable { expandedIdx = if (isOpen) null else idx }
                                    .padding(vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.question,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(text = if (isOpen) "▴" else "▾")
                                }
                                AnimatedVisibility(visible = isOpen) {
                                    Text(
                                        text = item.answer,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                            if (idx < filtered.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            }
                        }
                    }

                    ActiveModal.IHRAM -> {
                        ModalHeader("📜 ${strings.ihramModalTitle}") { viewModel.closeModal() }
                        DuaQuoteBox(
                            title = "❌ المحظورات:",
                            text = "قص الشعر والأظافر، الطيب، المخيط المفصل (للرجال)، تغطية الرأس بملاصق، النقاب والقفازان (للمرأة)، الصيد، وعقد النكاح.",
                            accentColor = StatusDanger
                        )
                        DuaQuoteBox(
                            title = "✅ يُباح:",
                            text = "الاغتسال، تبديل الإحرام، الحزام والساعة والنظارة، المظلة، وضماد الجروح.",
                            accentColor = StatusSuccess
                        )
                    }

                    ActiveModal.ID_CARD -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = strings.medicalIdTitle,
                                        color = SacredGold,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = state.profile.name,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text("• ${state.profile.nationality}", style = MaterialTheme.typography.bodyLarge)
                                Text("• ${strings.bloodPrefix} ${state.profile.bloodType}", style = MaterialTheme.typography.bodyLarge)
                                Text("• ${state.profile.medicalConditions}", style = MaterialTheme.typography.bodyLarge)
                                Text("• ${strings.savedHotelPrefix} ${state.hotelName}", style = MaterialTheme.typography.bodyLarge)
                                Text("• ${strings.mutawwifPrefix} ${state.profile.mutawwifPhone}", style = MaterialTheme.typography.bodyLarge)
                            }

                            PilgrimGoldButton(
                                text = strings.editBtn,
                                onClick = { viewModel.openModal(ActiveModal.PROFILE) },
                                modifier = Modifier.fillMaxWidth(),
                                tag = "id_card_edit_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.closeBtn,
                                onClick = { viewModel.closeModal() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    ActiveModal.TRANSLATOR -> {
                        ModalHeader("🗣️ ${strings.translatorModalTitle}") { viewModel.closeModal() }
                        PilgrimContent.phrases.forEachIndexed { idx, phrase ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .minimumInteractiveComponentSize()
                                    .testTag("phrase_card_$idx")
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.speak(phrase.arabic) },
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "🔊 ${phrase.arabic}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = phrase.english,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    ActiveModal.SHOE -> {
                        var shoeText by remember(state.shoeLocation) {
                            mutableStateOf(if (state.shoeLocation == "لم يتم التحديد") "" else state.shoeLocation)
                        }
                        ModalHeader("👟 ${strings.shoeModalTitle}") { viewModel.closeModal() }
                        OutlinedTextField(
                            value = shoeText,
                            onValueChange = { shoeText = it },
                            placeholder = { Text("مثال: باب الملك فهد - رف 14") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("shoe_input")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PilgrimGoldButton(
                                text = strings.saveBtn,
                                onClick = { viewModel.saveShoeLocation(shoeText) },
                                modifier = Modifier.weight(1f),
                                tag = "save_shoe_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.cancelBtn,
                                onClick = { viewModel.closeModal() }
                            )
                        }
                    }

                    ActiveModal.HOTEL -> {
                        var hotelInput by remember(state.hotelName) { mutableStateOf(state.hotelName) }
                        ModalHeader("🏨 ${strings.hotelModalTitle}") { viewModel.closeModal() }
                        val gpsMsg = when {
                            state.hotelDistanceMeters != null && state.hotelLat != null ->
                                "✓ GPS • ${state.hotelDistanceMeters.roundToInt()} m"
                            state.hotelLat != null -> "✓ GPS"
                            else -> "GPS"
                        }
                        Text(
                            text = gpsMsg,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = hotelInput,
                            onValueChange = { hotelInput = it },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hotel_input")
                        )
                        PilgrimOutlineButton(
                            text = "📍 ${strings.gpsBtn}",
                            onClick = { viewModel.saveHotelGps() },
                            modifier = Modifier.fillMaxWidth(),
                            tag = "save_hotel_gps_btn"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PilgrimGoldButton(
                                text = strings.saveBtn,
                                onClick = { viewModel.saveHotelName(hotelInput) },
                                modifier = Modifier.weight(1f),
                                tag = "save_hotel_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.closeBtn,
                                onClick = { viewModel.closeModal() }
                            )
                        }
                    }

                    ActiveModal.SOS -> {
                        ModalHeader("🆘 ${strings.sosModalTitle}") { viewModel.closeModal() }
                        Text(
                            text = "${state.profile.name}\n${strings.savedHotelPrefix} ${state.hotelName}\n${strings.mutawwifPrefix} ${state.profile.mutawwifPhone}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        PilgrimGoldButton(
                            text = "📞 ${strings.sosCall911}",
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:911")))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            tag = "sos_call_911_btn",
                            containerColor = StatusDanger,
                            contentColor = Color.White
                        )
                        PilgrimOutlineButton(
                            text = "📞 ${strings.sosCallMutawwif}",
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${state.profile.mutawwifPhone}")))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            tag = "sos_call_mutawwif_btn"
                        )
                        PilgrimOutlineButton(
                            text = strings.closeBtn,
                            onClick = { viewModel.closeModal() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    ActiveModal.PROFILE -> {
                        var name by remember(state.profile) { mutableStateOf(state.profile.name) }
                        var nat by remember(state.profile) { mutableStateOf(state.profile.nationality) }
                        var blood by remember(state.profile) { mutableStateOf(state.profile.bloodType) }
                        var med by remember(state.profile) { mutableStateOf(state.profile.medicalConditions) }
                        var phone by remember(state.profile) { mutableStateOf(state.profile.mutawwifPhone) }

                        ModalHeader(strings.profileModalTitle) { viewModel.closeModal() }
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("الاسم / Name") }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = nat, onValueChange = { nat = it }, label = { Text("الجنسية / Nationality") }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = blood, onValueChange = { blood = it }, label = { Text("${strings.bloodPrefix}") }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = med, onValueChange = { med = it }, label = { Text("الحالة الطبية / Medical") }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("${strings.mutawwifPrefix}") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PilgrimGoldButton(
                                text = strings.saveBtn,
                                onClick = {
                                    viewModel.saveProfile(PilgrimProfile(name, nat, blood, med, phone))
                                },
                                modifier = Modifier.weight(1f),
                                tag = "save_profile_btn"
                            )
                            PilgrimOutlineButton(
                                text = strings.cancelBtn,
                                onClick = { viewModel.closeModal() }
                            )
                        }
                    }

                    ActiveModal.NONE -> Unit
                }
            }
        }
    }
}

@Composable
private fun ModalHeader(title: String, onClose: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        PilgrimOutlineButton(text = "✕", onClick = onClose, tag = "modal_close_btn")
    }
}
