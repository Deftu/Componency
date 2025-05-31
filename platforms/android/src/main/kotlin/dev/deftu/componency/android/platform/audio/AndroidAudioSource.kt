package dev.deftu.componency.android.platform.audio

import dev.deftu.componency.platform.audio.AudioSource

public class AndroidAudioSource : AudioSource {

    override val sampleRate: Int
        get() = TODO("Not yet implemented")

    override val channelCount: Int
        get() = TODO("Not yet implemented")

    override val isEndOfStream: Boolean
        get() = TODO("Not yet implemented")

    override fun read(buffer: ByteArray, offset: Int, size: Int): Int {
        TODO("Not yet implemented")
    }

}
