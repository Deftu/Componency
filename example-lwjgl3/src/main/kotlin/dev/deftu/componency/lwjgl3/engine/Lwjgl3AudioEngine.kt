package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.platform.audio.AudioEngine
import dev.deftu.componency.platform.audio.AudioPlayer
import dev.deftu.componency.platform.audio.AudioSource
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

class Lwjgl3AudioEngine : AudioEngine {

    fun initialize() {
        @Suppress("CAST_NEVER_SUCCEEDS")
        val device = ALC10.alcOpenDevice(null as? ByteBuffer)
        if (device == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to open audio device")
        }

        val alcCapabilities = ALC.createCapabilities(device)
        if (!alcCapabilities.OpenALC10) {
            throw IllegalStateException("Failed to create OpenAL capabilities")
        }

        @Suppress("CAST_NEVER_SUCCEEDS")
        val context = ALC10.alcCreateContext(device, null as? IntArray)
        if (context == MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device)
            throw IllegalStateException("Failed to create OpenAL context")
        }

        ALC10.alcMakeContextCurrent(context)
        if (ALC10.alcGetError(device) != ALC10.ALC_NO_ERROR) {
            ALC10.alcDestroyContext(context)
            ALC10.alcCloseDevice(device)
            throw IllegalStateException("Failed to make OpenAL context current")
        }

        AL.createCapabilities(alcCapabilities, MemoryUtil::memCallocPointer)
    }

    override fun createPlayer(source: AudioSource): AudioPlayer {
        return Lwjgl3AudioPlayer(source)
    }

}
