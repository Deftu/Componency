package dev.deftu.componency.platform.audio

public open class StreamingAudioSource(
    private val stream: AudioByteStream,
    override val sampleRate: Int,
    override val channelCount: Int,
) : AudioSource, AutoCloseable {

    override var isEndOfStream: Boolean = false
        protected set

    override fun read(buffer: ByteArray, offset: Int, size: Int): Int {
        if (isEndOfStream) {
            return -1
        }

        val readBytes = stream.read(buffer, offset, size)
        if (readBytes == -1) {
            isEndOfStream = true
            return -1
        }

        return readBytes
    }

    override fun close() {
        stream.close()
    }

}
