package dev.deftu.componency.android.platform.audio

import dev.deftu.componency.platform.audio.AudioEngine
import dev.deftu.componency.platform.audio.AudioPlayer
import dev.deftu.componency.platform.audio.AudioSource

public object AndroidAudioEngine : AudioEngine {

    override fun createPlayer(source: AudioSource): AudioPlayer {
        return AndroidAudioPlayer(source)
    }

}
