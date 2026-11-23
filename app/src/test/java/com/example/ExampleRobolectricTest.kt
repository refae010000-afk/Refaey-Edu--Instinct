package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.ActivityCalorieCalculator
import com.example.util.GeoMath
import com.example.util.PrayerCalculator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("رفيق المعتمر", appName)
    }

    @Test
    fun `prayer calculator returns all six prayers`() {
        val schedule = PrayerCalculator.calculate(21.4225, 39.8262)
        assertEquals(6, schedule.allPrayers.size)
        assertTrue(schedule.nextName.isNotBlank())
        assertTrue(schedule.countdownFormatted.contains(":"))
    }

    @Test
    fun `nearest miqat from madinah is dhul hulayfah`() {
        val (miqat, distanceKm) = GeoMath.nearestMiqat(24.4672, 39.6112)
        assertTrue(miqat.contains("ذو الحليفة"))
        assertTrue(distanceKm < 20.0)
    }

    @Test
    fun `calorie calculator computes tawaf and sai calories accurately`() {
        val stats = ActivityCalorieCalculator.calculate(
            tawafRounds = 7,
            saiRounds = 7,
            totalKm = 4.55,
            weightKg = 70
        )
        assertEquals(1.75, stats.tawafDistanceKm, 0.001)
        assertEquals(2.80, stats.saiDistanceKm, 0.001)
        assertEquals(4.55, stats.totalDistanceKm, 0.001)
        assertTrue(stats.totalCalories > 200)
        assertTrue(stats.estimatedSteps > 5000)
    }

    @Test
    fun `hijri date helper returns formatted arabic hijri string`() {
        val hijri = com.example.util.HijriDateHelper.todayHijriFormatted()
        assertTrue(hijri.endsWith("هـ"))
    }

    @Test
    fun `global strings populated for all six languages`() {
        com.example.model.AppLanguage.entries.forEach { lang ->
            val s = com.example.data.GlobalStrings.forLang(lang)
            assertTrue(s.appTitle.isNotBlank())
            assertTrue(s.quickToolsSectionTitle.isNotBlank())
            assertTrue(s.filterAll.isNotBlank())
            assertEquals(6, s.prayerNames.size)
        }
    }

    @Test
    fun `heat alert evaluator flags danger temperatures and triggers push`() {
        val safe = com.example.util.HeatAlertEvaluator.evaluateDanger(
            tempC = 29,
            feelsLikeC = 30,
            uvIndex = 5,
            dangerThresholdC = 40
        )
        assertEquals(com.example.model.HeatDangerLevel.SAFE, safe)

        val hiHot = com.example.util.HeatAlertEvaluator.computeHeatIndexC(tempC = 42, humidityPct = 45)
        assertTrue(hiHot >= 42)

        val danger = com.example.util.HeatAlertEvaluator.evaluateDanger(
            tempC = 42,
            feelsLikeC = hiHot,
            uvIndex = 10,
            dangerThresholdC = 40
        )
        assertTrue(
            danger == com.example.model.HeatDangerLevel.DANGER ||
                danger == com.example.model.HeatDangerLevel.EXTREME
        )
        assertTrue(
            com.example.util.HeatAlertEvaluator.shouldPushAlert(
                dangerLevel = danger,
                tempC = 42,
                feelsLikeC = hiHot,
                dangerThresholdC = 40
            )
        )
    }

    @Test
    fun `weather alert notifier registers notification channel`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        com.example.util.WeatherAlertNotifier.ensureChannel(context, com.example.model.AppLanguage.AR)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channel = nm.getNotificationChannel(com.example.util.WeatherAlertNotifier.CHANNEL_ID)
        assertTrue(channel != null)
    }

    @Test
    fun `global 3d infrastructure labels and infos exist for all languages`() {
        com.example.model.AppLanguage.entries.forEach { lang ->
            val s = com.example.data.GlobalStrings.forLang(lang)
            assertTrue(s.servicesLayerToggleLabel.isNotBlank())
            assertTrue(s.legendBusRoute.isNotBlank())
            assertTrue(s.legendAmbulanceHub.isNotBlank())
            assertTrue(s.scene3DLabels.busRouteLabel.isNotBlank())
            assertTrue(s.scene3DLabels.ambulanceHubLabel.isNotBlank())
            assertTrue(s.scene3DLabels.waterMistLabel.isNotBlank())
            assertTrue(s.scene3DLabels.trainStationLabel.isNotBlank())
            com.example.model.Scene3DType.entries.forEach { scene ->
                val info = s.scene3DInfos[scene]
                assertTrue(info != null && info.title.isNotBlank() && info.description.isNotBlank())
            }
        }
    }

    @Test
    fun `view model toggles 3d services layer state`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.PilgrimViewModel(app)
        assertTrue(vm.uiState.value.show3DServicesLayer)
        vm.toggle3DServicesLayer()
        assertTrue(!vm.uiState.value.show3DServicesLayer)
        vm.toggle3DServicesLayer()
        assertTrue(vm.uiState.value.show3DServicesLayer)
    }

    @Test
    fun `compass math shortest angle delta handles 0 and 360 wrap around`() {
        assertEquals(4f, com.example.util.CompassMath.shortestAngleDelta(358f, 2f), 0.01f)
        assertEquals(-4f, com.example.util.CompassMath.shortestAngleDelta(2f, 358f), 0.01f)
        assertEquals(-2f, com.example.util.CompassMath.shortestAngleDelta(725f, 3f), 0.01f)
        assertTrue(com.example.util.CompassMath.isAlignedWithQibla(359f, 2f))
        assertTrue(!com.example.util.CompassMath.isAlignedWithQibla(340f, 2f))
    }

    @Test
    fun `compass math computes orientation from accelerometer and magnetometer vectors`() {
        val gravityFlat = floatArrayOf(0f, 0f, 9.81f)
        val magNorth = floatArrayOf(0f, 40f, -20f)
        val northOrient = com.example.util.CompassMath.computeOrientationFromSensors(gravityFlat, magNorth)
        assertTrue(northOrient != null)
        assertTrue(northOrient!!.azimuthDeg < 5f || northOrient.azimuthDeg > 355f)

        val magEast = floatArrayOf(-40f, 0f, -20f)
        val eastOrient = com.example.util.CompassMath.computeOrientationFromSensors(gravityFlat, magEast)
        assertTrue(eastOrient != null)
        assertEquals(90f, eastOrient!!.azimuthDeg, 2f)
    }

    @Test
    fun `compass math low pass filter smooths step input`() {
        val output = floatArrayOf(0f, 0f, 0f)
        com.example.util.CompassMath.lowPassFilter(floatArrayOf(10f, -10f, 20f), output, alpha = 0.2f)
        assertEquals(2f, output[0], 0.001f)
        assertEquals(-2f, output[1], 0.001f)
        assertEquals(4f, output[2], 0.001f)
    }

    @Test
    fun `view model reference city computes accurate qibla bearing and distance`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.PilgrimViewModel(app)
        vm.selectQiblaReferenceCity(com.example.model.QiblaReferenceCity.MADINAH)
        val st = vm.uiState.value
        assertTrue(st.qiblaActive)
        assertEquals(com.example.model.QiblaLocationSource.CITY, st.qiblaLocationSource)
        assertEquals(176.6f, st.qiblaBearingDeg ?: 0f, 3f)
        assertTrue((st.qiblaDistanceMeters ?: 0.0) > 300_000.0)
    }

    @Test
    fun `qibla compass strings populated for all six languages`() {
        com.example.model.AppLanguage.entries.forEach { lang ->
            val s = com.example.data.GlobalStrings.forLang(lang)
            assertTrue(s.qiblaHeadingLabel.isNotBlank())
            assertTrue(s.qiblaCalibrateTitle.isNotBlank())
            assertEquals(4, s.qiblaCardinalLabels.size)
        }
    }

    @Test
    fun `localized ritual guide data complete for all six languages and five nusuk types`() {
        com.example.model.AppLanguage.entries.forEach { lang ->
            val strings = com.example.data.GlobalStrings.forLang(lang)
            assertTrue(strings.guideLanguageLabel.isNotBlank())
            assertTrue(strings.guideVisualDescLabel.isNotBlank())
            assertTrue(strings.guideActionStepsLabel.isNotBlank())
            assertTrue(strings.guideCompleteNextBtn.isNotBlank())
            assertEquals(3, com.example.data.LocalizedRitualGuideData.jamaratNamesFor(lang).size)

            com.example.model.TawafFloor.entries.forEach { floor ->
                assertTrue(com.example.data.LocalizedRitualGuideData.tawafFloorLabelFor(lang, floor).isNotBlank())
            }
            com.example.model.StepIllustrationKey.entries.forEach { key ->
                assertTrue(com.example.data.LocalizedRitualGuideData.illustrationLabelsFor(lang, key).isNotEmpty())
            }
            com.example.model.CounterMode.entries.forEach { mode ->
                assertEquals(7, com.example.data.LocalizedRitualGuideData.roundDuasFor(lang, mode).size)
            }
            com.example.model.NusukType.entries.forEach { nusuk ->
                val steps = com.example.data.LocalizedRitualGuideData.ritualStepsFor(lang, nusuk)
                assertTrue("Expected steps for $lang/$nusuk", steps.isNotEmpty())
                steps.forEach { step ->
                    assertTrue(step.title.isNotBlank())
                    assertTrue(step.description.isNotBlank())
                    assertTrue("Missing visualCaption for $lang/${step.id}", step.visualCaption.isNotBlank())
                    assertTrue("Missing detailedPoints for $lang/${step.id}", step.detailedPoints.isNotEmpty())
                    assertTrue("Missing stageDua for $lang/${step.id}", step.stageDua.isNotBlank())
                }
            }
        }
    }

    @Test
    fun `datastore persists selected language across all six languages`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.PilgrimPreferencesRepository(context)
        com.example.model.AppLanguage.entries.forEach { lang ->
            repo.setLanguage(lang)
            val savedLang = repo.languageFlow.first()
            val stored = repo.dataFlow.first()
            assertEquals(lang, savedLang)
            assertEquals(lang, stored.language)
        }
    }

    @Test
    fun `view model manages interactive ritual guide steps and language switching`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val vm = com.example.ui.PilgrimViewModel(app)

        vm.setLanguage(com.example.model.AppLanguage.FR)
        assertEquals(com.example.model.AppLanguage.FR, vm.uiState.value.language)

        val steps = com.example.data.LocalizedRitualGuideData.ritualStepsFor(
            com.example.model.AppLanguage.FR,
            com.example.model.NusukType.UMRAH
        )
        assertEquals(0, vm.uiState.value.activeGuideStepIndex)

        vm.nextGuideStep(steps.size)
        assertEquals(1, vm.uiState.value.activeGuideStepIndex)

        vm.completeAndAdvanceGuideStep(steps[1].id, steps.size)
        assertTrue(steps[1].id in vm.uiState.value.completedSteps)
        assertEquals(2, vm.uiState.value.activeGuideStepIndex)

        vm.previousGuideStep()
        assertEquals(1, vm.uiState.value.activeGuideStepIndex)

        vm.toggleGuideViewMode()
        assertTrue(vm.uiState.value.guideShowAllStages)

        vm.selectGuideStep(3, steps.size)
        assertEquals(3, vm.uiState.value.activeGuideStepIndex)
        assertTrue(!vm.uiState.value.guideShowAllStages)

        vm.setNusukType(com.example.model.NusukType.TAMATTU)
        assertEquals(com.example.model.NusukType.TAMATTU, vm.uiState.value.selectedNusuk)
        assertEquals(0, vm.uiState.value.activeGuideStepIndex)
    }

    @Test
    fun `offline map catalog populates landmarks across all six languages and three regions`() {
        com.example.model.AppLanguage.entries.forEach { lang ->
            val mapStrings = com.example.data.OfflineMapCatalog.stringsFor(lang)
            assertTrue(mapStrings.cardTitle.isNotBlank())
            assertTrue(mapStrings.offlineReadyBadge.isNotBlank())

            com.example.model.OfflineMapRegion.entries.forEach { region ->
                val list = com.example.data.OfflineMapCatalog.landmarksFor(lang, region)
                assertTrue("Expected landmarks for $lang/$region", list.isNotEmpty())
                assertTrue("Expected primary hub for $region", list.any { it.isPrimaryHub })
                list.forEach { lm ->
                    assertTrue(lm.name.isNotBlank())
                    assertTrue(lm.shortLabel.isNotBlank())
                    assertTrue(lm.description.isNotBlank())
                    assertTrue(lm.ritualTip.isNotBlank())
                    assertTrue(
                        "Landmark ${lm.id} out of region bounds",
                        com.example.data.OfflineMapMath.isWithinRegion(lm.lat, lm.lng, region)
                    )
                }
            }
        }
    }

    @Test
    fun `offline map projection and distance calculations are accurate without network`() {
        val (nx, ny) = com.example.data.OfflineMapMath.project(
            GeoMath.KAABA_LAT,
            GeoMath.KAABA_LNG,
            com.example.model.OfflineMapRegion.HARAM_PRECINCT
        )
        assertTrue(nx in 0.1f..0.9f)
        assertTrue(ny in 0.1f..0.9f)

        val mashaaerLandmarks = com.example.data.OfflineMapCatalog.landmarksFor(
            com.example.model.AppLanguage.EN,
            com.example.model.OfflineMapRegion.MAKKAH_MASHAAER
        )
        val haram = mashaaerLandmarks.first { it.id == "m_haram" }
        val arafat = mashaaerLandmarks.first { it.id == "m_arafat" }
        assertEquals(0.0, com.example.data.OfflineMapMath.distanceFromRegionHubMeters(haram), 1.0)
        val arafatDist = com.example.data.OfflineMapMath.distanceFromRegionHubMeters(arafat)
        assertTrue("Expected Arafat ~18 km from Haram, was $arafatDist", arafatDist in 15_000.0..23_000.0)
    }
}

