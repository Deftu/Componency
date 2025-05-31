package dev.deftu.componency.android.platform.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import dev.deftu.componency.platform.audio.AudioPlayer
import dev.deftu.componency.platform.audio.AudioSource
import java.util.concurrent.atomic.AtomicBoolean

public class AndroidAudioPlayer(
    private val source: AudioSource
) : AudioPlayer {

    private val bufferSizeInBytes: Int
    private val audioTrack: AudioTrack
    private val playing = AtomicBoolean(false)
    private var playbackThread: Thread? = null

    override var volume: Float = 1.0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            audioTrack.setVolume(field)
        }

    override val isPlaying: Boolean
        get() = playing.get()

    init {
        val format = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(source.sampleRate)
            .setChannelMask(
                when (source.channelCount) {
                    1 -> AudioFormat.CHANNEL_OUT_MONO
                    2 -> AudioFormat.CHANNEL_OUT_STEREO
                    else -> AudioFormat.CHANNEL_OUT_DEFAULT
                }
            )
            .build()

        bufferSizeInBytes = AudioTrack.getMinBufferSize(
            source.sampleRate,
            format.channelMask,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(format)
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    override fun play() {
        if (playing.get()) return

        playing.set(true)
        audioTrack.play()

        playbackThread = Thread {
            val buffer = ByteArray(bufferSizeInBytes)
            while (playing.get() && !source.isEndOfStream) {
                val bytesRead = source.read(buffer, 0, buffer.size)
                if (bytesRead > 0) {
                    audioTrack.write(buffer, 0, bytesRead)
                } else {
                    break
                }
            }
            stop()
        }
        playbackThread?.start()
    }

    override fun pause() {
        if (!playing.get()) return
        playing.set(false)
        audioTrack.pause()
    }

    override fun stop() {
        if (!playing.get()) return
        playing.set(false)
        audioTrack.stop()
        audioTrack.flush()
    }

    override fun close() {
        stop()
        audioTrack.release()
    }

}
