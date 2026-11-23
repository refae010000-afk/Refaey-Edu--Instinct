package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.CounterMode
import com.example.model.NusukType
import com.example.model.PilgrimProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.pilgrimDataStore: DataStore<Preferences> by preferencesDataStore(name = "pilgrim_companion_prefs")

data class StoredPilgrimData(
    val language: AppLanguage = AppLanguage.AR,
    val darkMode: Boolean = false,
    val elderMode: Boolean = false,
    val nusukType: NusukType = NusukType.UMRAH,
    val counterMode: CounterMode = CounterMode.TAWAF,
    val round: Int = 1,
    val jamCounts: List<Int> = listOf(0, 0, 0),
    val tasbih: Int = 0,
    val totalKm: Double = 0.0,
    val tawafTotalRounds: Int = 0,
    val saiTotalRounds: Int = 0,
    val weightKg: Int = 70,
    val completedStepIds: Set<String> = emptySet(),
    val shoeLocation: String = "لم يتم التحديد",
    val hotelName: String = "أبراج الساعة",
    val hotelLat: Double? = null,
    val hotelLng: Double? = null,
    val profile: PilgrimProfile = PilgrimProfile(),
    val lovedOnes: List<String> = listOf("أمي وأبي: اللهم اغفر لهما وارحمهما"),
    val checklistMap: Map<String, Boolean> = mapOf("p1" to true, "p2" to true, "p3" to false, "p4" to false, "p5" to false),
    val weatherAlertsEnabled: Boolean = false,
    val weatherDangerThresholdC: Int = 40,
    val lastUserLat: Double? = null,
    val lastUserLng: Double? = null,
    val lastHeatAlertMs: Long = 0L,
    val activeGuideStepIndex: Int = 0
)

class PilgrimPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANG = stringPreferencesKey("lang")
        val DARK = booleanPreferencesKey("dark")
        val ELDER = booleanPreferencesKey("elder")
        val NUSUK = stringPreferencesKey("nusuk")
        val COUNTER_MODE = stringPreferencesKey("c_mode")
        val ROUND = intPreferencesKey("round")
        val JAM_0 = intPreferencesKey("jam_0")
        val JAM_1 = intPreferencesKey("jam_1")
        val JAM_2 = intPreferencesKey("jam_2")
        val TASBIH = intPreferencesKey("tasbih")
        val KM = doublePreferencesKey("km")
        val TAWAF_TOTAL_ROUNDS = intPreferencesKey("tawaf_total_rounds")
        val SAI_TOTAL_ROUNDS = intPreferencesKey("sai_total_rounds")
        val WEIGHT_KG = intPreferencesKey("weight_kg")
        val COMPLETED_STEPS = stringSetPreferencesKey("completed_steps")
        val SHOE = stringPreferencesKey("shoe")
        val HOTEL_NAME = stringPreferencesKey("hotel_name")
        val HOTEL_LAT = doublePreferencesKey("hotel_lat")
        val HOTEL_LNG = doublePreferencesKey("hotel_lng")
        val P_NAME = stringPreferencesKey("p_name")
        val P_NAT = stringPreferencesKey("p_nat")
        val P_BLOOD = stringPreferencesKey("p_blood")
        val P_MED = stringPreferencesKey("p_med")
        val P_PHONE = stringPreferencesKey("p_phone")
        val LOVED_ONES = stringPreferencesKey("loved_ones_joined")
        val CHK_TRUE = stringSetPreferencesKey("chk_true")
        val CHK_TOUCHED = booleanPreferencesKey("chk_touched")
        val WEATHER_ALERTS_ENABLED = booleanPreferencesKey("weather_alerts_enabled")
        val WEATHER_DANGER_THRESHOLD = intPreferencesKey("weather_danger_threshold_c")
        val LAST_USER_LAT = doublePreferencesKey("last_user_lat")
        val LAST_USER_LNG = doublePreferencesKey("last_user_lng")
        val LAST_HEAT_ALERT_MS = longPreferencesKey("last_heat_alert_ms")
        val ACTIVE_GUIDE_STEP = intPreferencesKey("active_guide_step")
    }

    private val sep = "\u001F"

    val languageFlow: Flow<AppLanguage> = context.pilgrimDataStore.data.map { p ->
        AppLanguage.fromCode(p[Keys.LANG] ?: AppLanguage.AR.code)
    }

    val dataFlow: Flow<StoredPilgrimData> = context.pilgrimDataStore.data.map { p ->
        val lovedRaw = p[Keys.LOVED_ONES]
        val lovedList = if (lovedRaw == null) {
            listOf("أمي وأبي: اللهم اغفر لهما وارحمهما")
        } else if (lovedRaw.isEmpty()) {
            emptyList()
        } else {
            lovedRaw.split(sep)
        }

        val chkMap = if (p[Keys.CHK_TOUCHED] == true) {
            val trueIds = p[Keys.CHK_TRUE] ?: emptySet()
            PilgrimContent.checklistItems.associate { it.id to (it.id in trueIds) }
        } else {
            PilgrimContent.checklistItems.associate { it.id to it.defaultChecked }
        }

        StoredPilgrimData(
            language = AppLanguage.fromCode(p[Keys.LANG] ?: "ar"),
            darkMode = p[Keys.DARK] ?: false,
            elderMode = p[Keys.ELDER] ?: false,
            nusukType = NusukType.entries.find { it.id == p[Keys.NUSUK] } ?: NusukType.UMRAH,
            counterMode = CounterMode.entries.find { it.id == p[Keys.COUNTER_MODE] } ?: CounterMode.TAWAF,
            round = (p[Keys.ROUND] ?: 1).coerceIn(1, 7),
            jamCounts = listOf(
                (p[Keys.JAM_0] ?: 0).coerceIn(0, 7),
                (p[Keys.JAM_1] ?: 0).coerceIn(0, 7),
                (p[Keys.JAM_2] ?: 0).coerceIn(0, 7)
            ),
            tasbih = p[Keys.TASBIH] ?: 0,
            totalKm = p[Keys.KM] ?: 0.0,
            tawafTotalRounds = (p[Keys.TAWAF_TOTAL_ROUNDS] ?: 0).coerceAtLeast(0),
            saiTotalRounds = (p[Keys.SAI_TOTAL_ROUNDS] ?: 0).coerceAtLeast(0),
            weightKg = (p[Keys.WEIGHT_KG] ?: 70).coerceIn(35, 180),
            completedStepIds = p[Keys.COMPLETED_STEPS] ?: emptySet(),
            shoeLocation = p[Keys.SHOE] ?: "لم يتم التحديد",
            hotelName = p[Keys.HOTEL_NAME] ?: "أبراج الساعة",
            hotelLat = p[Keys.HOTEL_LAT],
            hotelLng = p[Keys.HOTEL_LNG],
            profile = PilgrimProfile(
                name = p[Keys.P_NAME] ?: "عبدالله محمد العتيبي",
                nationality = p[Keys.P_NAT] ?: "سعودي",
                bloodType = p[Keys.P_BLOOD] ?: "O+",
                medicalConditions = p[Keys.P_MED] ?: "لا يوجد",
                mutawwifPhone = p[Keys.P_PHONE] ?: "0500000000"
            ),
            lovedOnes = lovedList,
            checklistMap = chkMap,
            weatherAlertsEnabled = p[Keys.WEATHER_ALERTS_ENABLED] ?: false,
            weatherDangerThresholdC = (p[Keys.WEATHER_DANGER_THRESHOLD] ?: 40).coerceIn(32, 48),
            lastUserLat = p[Keys.LAST_USER_LAT],
            lastUserLng = p[Keys.LAST_USER_LNG],
            lastHeatAlertMs = p[Keys.LAST_HEAT_ALERT_MS] ?: 0L,
            activeGuideStepIndex = (p[Keys.ACTIVE_GUIDE_STEP] ?: 0).coerceAtLeast(0)
        )
    }

    suspend fun setLanguage(lang: AppLanguage) {
        context.pilgrimDataStore.edit { it[Keys.LANG] = lang.code }
    }

    suspend fun setActiveGuideStep(index: Int) {
        context.pilgrimDataStore.edit { it[Keys.ACTIVE_GUIDE_STEP] = index.coerceAtLeast(0) }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.pilgrimDataStore.edit { it[Keys.DARK] = enabled }
    }

    suspend fun setElderMode(enabled: Boolean) {
        context.pilgrimDataStore.edit { it[Keys.ELDER] = enabled }
    }

    suspend fun setNusuk(nusuk: NusukType) {
        context.pilgrimDataStore.edit { it[Keys.NUSUK] = nusuk.id }
    }

    suspend fun setCounterMode(mode: CounterMode) {
        context.pilgrimDataStore.edit { it[Keys.COUNTER_MODE] = mode.id }
    }

    suspend fun setRoundAndActivity(
        round: Int,
        km: Double,
        tawafTotalRounds: Int,
        saiTotalRounds: Int
    ) {
        context.pilgrimDataStore.edit {
            it[Keys.ROUND] = round
            it[Keys.KM] = km.coerceAtLeast(0.0)
            it[Keys.TAWAF_TOTAL_ROUNDS] = tawafTotalRounds.coerceAtLeast(0)
            it[Keys.SAI_TOTAL_ROUNDS] = saiTotalRounds.coerceAtLeast(0)
        }
    }

    suspend fun setWeightKg(weightKg: Int) {
        context.pilgrimDataStore.edit {
            it[Keys.WEIGHT_KG] = weightKg.coerceIn(35, 180)
        }
    }

    suspend fun resetActivityStats() {
        context.pilgrimDataStore.edit {
            it[Keys.KM] = 0.0
            it[Keys.TAWAF_TOTAL_ROUNDS] = 0
            it[Keys.SAI_TOTAL_ROUNDS] = 0
        }
    }

    suspend fun setJamCounts(counts: List<Int>) {
        context.pilgrimDataStore.edit {
            it[Keys.JAM_0] = counts.getOrElse(0) { 0 }
            it[Keys.JAM_1] = counts.getOrElse(1) { 0 }
            it[Keys.JAM_2] = counts.getOrElse(2) { 0 }
        }
    }

    suspend fun setTasbih(count: Int) {
        context.pilgrimDataStore.edit { it[Keys.TASBIH] = count }
    }

    suspend fun toggleStepDone(stepId: String) {
        context.pilgrimDataStore.edit { p ->
            val current = p[Keys.COMPLETED_STEPS]?.toMutableSet() ?: mutableSetOf()
            if (stepId in current) current.remove(stepId) else current.add(stepId)
            p[Keys.COMPLETED_STEPS] = current
        }
    }

    suspend fun markStepDone(stepId: String) {
        context.pilgrimDataStore.edit { p ->
            val current = p[Keys.COMPLETED_STEPS]?.toMutableSet() ?: mutableSetOf()
            current.add(stepId)
            p[Keys.COMPLETED_STEPS] = current
        }
    }

    suspend fun saveShoe(location: String) {
        context.pilgrimDataStore.edit {
            it[Keys.SHOE] = location.ifBlank { "باب الملك فهد - رف 14" }
        }
    }

    suspend fun saveHotelName(name: String) {
        context.pilgrimDataStore.edit {
            it[Keys.HOTEL_NAME] = name.ifBlank { "أبراج الساعة" }
        }
    }

    suspend fun saveHotelGps(lat: Double, lng: Double) {
        context.pilgrimDataStore.edit {
            it[Keys.HOTEL_LAT] = lat
            it[Keys.HOTEL_LNG] = lng
        }
    }

    suspend fun saveProfile(profile: PilgrimProfile) {
        context.pilgrimDataStore.edit {
            it[Keys.P_NAME] = profile.name.ifBlank { "عبدالله محمد العتيبي" }
            it[Keys.P_NAT] = profile.nationality.ifBlank { "سعودي" }
            it[Keys.P_BLOOD] = profile.bloodType.ifBlank { "O+" }
            it[Keys.P_MED] = profile.medicalConditions.ifBlank { "لا يوجد" }
            it[Keys.P_PHONE] = profile.mutawwifPhone.ifBlank { "0500000000" }
        }
    }

    suspend fun saveLovedOnes(list: List<String>) {
        context.pilgrimDataStore.edit {
            it[Keys.LOVED_ONES] = list.joinToString(sep)
        }
    }

    suspend fun toggleChecklist(id: String, checked: Boolean) {
        context.pilgrimDataStore.edit { p ->
            val currentTrue = if (p[Keys.CHK_TOUCHED] == true) {
                p[Keys.CHK_TRUE]?.toMutableSet() ?: mutableSetOf()
            } else {
                PilgrimContent.checklistItems.filter { it.defaultChecked }.map { it.id }.toMutableSet()
            }
            if (checked) currentTrue.add(id) else currentTrue.remove(id)
            p[Keys.CHK_TOUCHED] = true
            p[Keys.CHK_TRUE] = currentTrue
        }
    }

    suspend fun setWeatherAlertsEnabled(enabled: Boolean) {
        context.pilgrimDataStore.edit { it[Keys.WEATHER_ALERTS_ENABLED] = enabled }
    }

    suspend fun setWeatherDangerThresholdC(thresholdC: Int) {
        context.pilgrimDataStore.edit { it[Keys.WEATHER_DANGER_THRESHOLD] = thresholdC.coerceIn(32, 48) }
    }

    suspend fun saveLastUserCoordinates(lat: Double, lng: Double) {
        context.pilgrimDataStore.edit {
            it[Keys.LAST_USER_LAT] = lat
            it[Keys.LAST_USER_LNG] = lng
        }
    }

    suspend fun saveLastHeatAlertMs(epochMs: Long) {
        context.pilgrimDataStore.edit { it[Keys.LAST_HEAT_ALERT_MS] = epochMs }
    }
}
