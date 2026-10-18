package com.example.ui.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.data.model.AmbientMusic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

object AmbientPlayer {
    private var job: Job? = null
    private var isPlaying = false

    fun isMusicPlaying(): Boolean = isPlaying

    fun play(music: AmbientMusic, scope: CoroutineScope) {
        stop()
        if (music == AmbientMusic.NONE) return

        isPlaying = true
        job = scope.launch(Dispatchers.Default) {
            val sampleRate = 44100
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val track = AudioTrack.Builder()
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
                .setBufferSizeInBytes(minBufferSize * 2)
                .build()

            track.play()

            // Musical chords/scales depending on mood
            val notes = when (music) {
                AmbientMusic.ROMANTIC_MELODY -> listOf(261.63, 329.63, 392.00, 493.88, 523.25, 392.00, 329.63) // Cmaj7 arpeggio
                AmbientMusic.BIRTHDAY_CHIME -> listOf(261.63, 261.63, 293.66, 261.63, 349.23, 329.63) // Birthday melody notes
                AmbientMusic.NOSTALGIC_ACOUSTIC -> listOf(220.00, 261.63, 329.63, 440.00, 329.63) // Am pentatonic
                AmbientMusic.LOFI_CHILL -> listOf(196.00, 246.94, 293.66, 370.00, 440.00) // Gmaj9 chill
                AmbientMusic.NONE -> emptyList()
            }

            try {
                var noteIndex = 0
                while (isActive && isPlaying) {
                    val freq = notes[noteIndex % notes.size]
                    noteIndex++
                    val durationMs = 380
                    val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
                    val buffer = ShortArray(numSamples)

                    for (i in 0 until numSamples) {
                        // Soft bell/sine envelope
                        val t = i.toDouble() / sampleRate
                        val envelope = (1.0 - (i.toDouble() / numSamples)) * 0.25
                        val sample = (sin(2.0 * Math.PI * freq * t) * 32767.0 * envelope).toInt()
                        buffer[i] = sample.coerceIn(-32768, 32767).toShort()
                    }

                    track.write(buffer, 0, buffer.size)
                    delay(380)
                }
            } catch (e: Exception) {
                // Ignore audio termination
            } finally {
                try {
                    track.stop()
                    track.release()
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
    }

    fun stop() {
        isPlaying = false
        job?.cancel()
        job = null
    }
}
