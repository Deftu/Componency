package dev.deftu.componency.lwjgl3.engine.audio

import dev.deftu.componency.platform.audio.AudioPlayer
import dev.deftu.componency.platform.audio.AudioSource
import org.lwjgl.BufferUtils
import org.lwjgl.openal.AL10
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class Lwjgl3AudioPlayer(private val source: AudioSource) : AudioPlayer {

    private val format: Int = when (source.channelCount) {
        1 -> AL10.AL_FORMAT_MONO16
        2 -> AL10.AL_FORMAT_STEREO16
        else -> throw IllegalArgumentException("Unsupported channel count: ${source.channelCount}")
    }

    private val sourceId = AL10.alGenSources()
    private val buffers = IntArray(2) { AL10.alGenBuffers() }

    private var thread: Thread? = null

    override var isPlaying: Boolean = false
        private set

    override var volume: Float
        get() = AL10.alGetSourcef(sourceId, AL10.AL_GAIN)
        set(value) {
            AL10.alSourcef(sourceId, AL10.AL_GAIN, value.coerceIn(0f, 1f))
        }

    init {
        thread = thread(start = true, isDaemon = true) {
            val bufferData = BufferUtils.createByteBuffer(4096)

            var tailBufferId: Int? = null
            var isSilent = false

            // Pre-allocate the buffer
            for (buffer in buffers) {
                val readBytes = readInto(bufferData)
                if (readBytes > 0) {
                    bufferData.flip()
                    AL10.alBufferData(buffer, format, bufferData, source.sampleRate)
                    AL10.alSourceQueueBuffers(sourceId, buffer)
                    bufferData.clear()
                }
            }

            while (!Thread.interrupted()) {
                if (!isPlaying) {
                    Thread.sleep(10)
                    continue
                }

                val processed = AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_PROCESSED)
                repeat(processed) {
                    val bufferId = AL10.alSourceUnqueueBuffers(sourceId)
                    val readBytes = readInto(bufferData)
                    if (readBytes > 0) {
                        bufferData.flip()
                        AL10.alBufferData(bufferId, format, bufferData, source.sampleRate)
                        AL10.alSourceQueueBuffers(sourceId, bufferId)
                        bufferData.clear()
                    } else if (source.isEndOfStream && !isSilent) {
                        val silentBuffer = BufferUtils.createByteBuffer(bufferData.capacity())
                        for (i in 0 until silentBuffer.capacity()) {
                            silentBuffer.put(0)
                        }

                        silentBuffer.flip()
                        tailBufferId = AL10.alGenBuffers()
                        AL10.alBufferData(tailBufferId!!, format, silentBuffer, source.sampleRate)
                        AL10.alSourceQueueBuffers(sourceId, tailBufferId!!)
                        isSilent = true
                    }
                }

                if (AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_QUEUED) > 0 && AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
                    AL10.alSourcePlay(sourceId)
                }

                Thread.sleep(5)
            }
        }
    }

    override fun play() {
        if (isPlaying) {
            return
        }

        isPlaying = true
        AL10.alSourcePlay(sourceId)
    }

    override fun pause() {
        if (!isPlaying) {
            return
        }

        isPlaying = false
        AL10.alSourcePause(sourceId)
    }

    override fun stop() {
        if (!isPlaying) {
            return
        }

        isPlaying = false
        AL10.alSourceStop(sourceId)
        AL10.alSourceRewind(sourceId)
    }

    override fun close() {
        thread?.interrupt()
        thread?.join(100)

        AL10.alSourceStop(sourceId)
        AL10.alSourceUnqueueBuffers(sourceId, buffers)

        AL10.alDeleteSources(sourceId)
        buffers.forEach(AL10::alDeleteBuffers)
    }

    private fun readInto(buffer: ByteBuffer): Int {
        val temp = ByteArray(buffer.remaining())
        val readBytes = source.read(temp, 0, temp.size)

        buffer.clear()
        if (readBytes > 0) {
            buffer.put(temp, 0, readBytes)

            // Align padding to sample frame boundaries
            val frameSize = 2 * source.channelCount
            val remainder = readBytes % frameSize
            if (remainder != 0) {
                for (i in 0 until (frameSize - remainder)) {
                    buffer.put(0)
                }
            }
        }

        return readBytes
    }

}
