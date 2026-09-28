package pe.unsch.ceis.asistencia.ui.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.io.Closeable

enum class AttendanceFeedback {
    SUCCESS,
    DUPLICATE,
    ERROR,
}

class AudioHapticHelper(
    context: Context,
) : Closeable {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(VibratorManager::class.java).defaultVibrator
    } else {
        context.getSystemService(Vibrator::class.java)
    }
    private val toneGenerator: ToneGenerator? = runCatching {
        ToneGenerator(AudioManager.STREAM_MUSIC, TONE_VOLUME_PERCENT)
    }.getOrNull()
    private var closed = false

    @Synchronized
    fun perform(feedback: AttendanceFeedback) {
        if (closed) return

        val pattern = feedback.pattern()
        vibrate(pattern.vibrationPattern)
        toneGenerator?.startTone(pattern.toneType, pattern.toneDurationMillis)
    }

    @Suppress("DEPRECATION")
    private fun vibrate(pattern: LongArray) {
        if (!vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, NO_REPEAT))
        } else {
            vibrator.vibrate(pattern, NO_REPEAT)
        }
    }

    @Synchronized
    override fun close() {
        if (closed) return
        vibrator.cancel()
        toneGenerator?.release()
        closed = true
    }

    private data class FeedbackPattern(
        val vibrationPattern: LongArray,
        val toneType: Int,
        val toneDurationMillis: Int,
    )

    private fun AttendanceFeedback.pattern(): FeedbackPattern = when (this) {
        AttendanceFeedback.SUCCESS -> FeedbackPattern(
            vibrationPattern = longArrayOf(0, 50),
            toneType = ToneGenerator.TONE_DTMF_D,
            toneDurationMillis = 80,
        )

        AttendanceFeedback.DUPLICATE -> FeedbackPattern(
            vibrationPattern = longArrayOf(0, 100, 80, 100),
            toneType = ToneGenerator.TONE_DTMF_5,
            toneDurationMillis = 150,
        )

        AttendanceFeedback.ERROR -> FeedbackPattern(
            vibrationPattern = longArrayOf(0, 300),
            toneType = ToneGenerator.TONE_DTMF_1,
            toneDurationMillis = 300,
        )
    }

    private companion object {
        const val TONE_VOLUME_PERCENT = 80
        const val NO_REPEAT = -1
    }
}

