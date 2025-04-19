package dev.deftu.componency.platform.audio

public interface AudioEngine {

    public fun createPlayer(source: AudioSource): AudioPlayer

}
