package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class PilgrimTtsHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        val arSa = Locale.Builder().setLanguage("ar").setRegion("SA").build()
        val arFallback = Locale.Builder().setLanguage("ar").build()

        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val res = tts?.setLanguage(arSa)
                isReady = res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED
                if (!isReady) {
                    tts?.setLanguage(arFallback)
                    isReady = true
                }
                tts?.setSpeechRate(0.9f)
            }
        }
    }

    fun speak(text: String, langCode: String = "ar"): Boolean {
        val engine = tts ?: return false
        if (!isReady) return false
        val targetLocale = when (langCode) {
            "en" -> Locale.US
            "fr" -> Locale.FRANCE
            "ur" -> Locale.Builder().setLanguage("ur").setRegion("PK").build()
            "id" -> Locale.Builder().setLanguage("id").setRegion("ID").build()
            "tr" -> Locale.Builder().setLanguage("tr").setRegion("TR").build()
            else -> Locale.Builder().setLanguage("ar").setRegion("SA").build()
        }
        val langRes = engine.setLanguage(targetLocale)
        if (langRes == TextToSpeech.LANG_MISSING_DATA || langRes == TextToSpeech.LANG_NOT_SUPPORTED) {
            engine.setLanguage(Locale.Builder().setLanguage("ar").build())
        }
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "pilgrim_tts_${System.currentTimeMillis()}")
        return true
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
