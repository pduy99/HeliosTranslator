package com.helios.kmptranslator.core.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

actual class TextToSpeech(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context.applicationContext, this)

    actual fun speak(language: String, text: String, onComplete: () -> Unit) {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                onComplete()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
            }
        })

        tts.language = Locale.forLanguageTag(language)
        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    actual fun stopSpeaking() {
        tts.stop()
    }

    actual fun shutdown() {
        tts.shutdown()
    }

    override fun onInit(status: Int) {

    }
}