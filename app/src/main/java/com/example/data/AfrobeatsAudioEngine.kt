package com.example.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Synthesizes real-time Afrobeats rhythmic grooves (syncopated 3-2 clave shaker,
 * Amapiano/Afrobeats log-drum bassline, and marimba/kalimba chord plucks) using Android AudioTrack.
 */
class AfrobeatsAudioEngine(
    private val scope: CoroutineScope
) {
    private var playbackJob: Job? = null
    private var audioTrack: AudioTrack? = null

    fun startTrack(track: AfrobeatsTrack) {
        stop()
        playbackJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 22050
            val minBuffer = try {
                AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
            } catch (_: Throwable) {
                -1
            }
            if (minBuffer <= 0) return@launch

            val player = try {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBuffer * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } catch (_: Throwable) {
                null
            } ?: return@launch

            audioTrack = player
            try {
                player.play()
                val stepDurationSec = (60.0 / track.bpm.coerceAtLeast(80)) / 4.0 // 16th-note step
                val samplesPerStep = (sampleRate * stepDurationSec).toInt().coerceAtLeast(512)
                val buffer = ShortArray(samplesPerStep)

                // Afrobeats 16-step clave/rim pattern & log-drum trigger steps
                val claveSteps = setOf(0, 3, 6, 10, 12)
                val kickSteps = setOf(0, 6, 8, 14)
                val shakerSteps = setOf(2, 4, 7, 10, 14, 15)

                var stepIndex = 0
                while (isActive) {
                    val stepInBar = stepIndex % 16
                    val bassHz = track.bassNotesHz[(stepIndex / 4) % track.bassNotesHz.size]
                    val melodyHz = track.melodyNotesHz[stepIndex % track.melodyNotesHz.size]

                    val hasKickOrLogDrum = stepInBar in kickSteps
                    val hasClave = stepInBar in claveSteps
                    val hasShaker = stepInBar in shakerSteps

                    for (i in 0 until samplesPerStep) {
                        val t = i.toDouble() / sampleRate
                        val envFast = exp(-t * 26.0)
                        val envMed = exp(-t * 11.0)

                        // Log drum / sub-bass with slight pitch drop
                        val logDrumSignal = if (hasKickOrLogDrum) {
                            val pitchDropHz = bassHz * (1.0 + 0.8 * exp(-t * 45.0))
                            sin(2.0 * PI * pitchDropHz * t) * envMed * 0.50
                        } else {
                            0.0
                        }

                        // Syncopated Kalimba / Marimba pluck
                        val melodySignal = if (stepInBar % 2 == 0 || hasClave) {
                            (sin(2.0 * PI * melodyHz * t) + 0.3 * sin(4.0 * PI * melodyHz * t)) * envFast * 0.28
                        } else {
                            0.0
                        }

                        // Woodblock / Afrobeats clave rim
                        val claveSignal = if (hasClave) {
                            sin(2.0 * PI * 920.0 * t) * exp(-t * 55.0) * 0.22
                        } else {
                            0.0
                        }

                        // Shekere / shaker high-frequency pulse
                        val shakerSignal = if (hasShaker) {
                            (Random.nextDouble(-1.0, 1.0)) * exp(-t * 48.0) * 0.10
                        } else {
                            0.0
                        }

                        val mixed = (logDrumSignal + melodySignal + claveSignal + shakerSignal)
                            .coerceIn(-0.95, 0.95)
                        buffer[i] = (mixed * Short.MAX_VALUE).toInt().toShort()
                    }

                    player.write(buffer, 0, buffer.size)
                    stepIndex++
                }
            } catch (_: Throwable) {
                // Ignore audio hardware exceptions on headless JVM
            } finally {
                try {
                    player.stop()
                    player.release()
                } catch (_: Throwable) {
                }
            }
        }
    }

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Throwable) {
        }
        audioTrack = null
    }
}
