package dev.deftu.componency.platform.audio

public interface AudioPlayer : AutoCloseable {

    public val isPlaying: Boolean

    public var volume: Float

    public fun play()

    public fun pause()

    public fun stop()

}
