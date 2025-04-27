package dev.deftu.componency.platform.audio

public interface AudioSource {

    public val sampleRate: Int

    public val channelCount: Int

    public val isEndOfStream: Boolean

    public fun read(buffer: ByteArray, offset: Int, size: Int): Int

}
