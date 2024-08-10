package dev.deftu.componency.animations

import dev.deftu.componency.properties.Property

public abstract class AnimatingProperty<T>(
    public val easing: Easing,
    public val totalFrames: Int,
    public val delayFrames: Int
) : Property<T> {

    protected var completedFrames: Int = 0

    public val progress: Float
        get() = easing.ease(completedFrames.toFloat() / totalFrames.toFloat())

    public val isFinished: Boolean
        get() = completedFrames - delayFrames >= totalFrames

    public var isPaused: Boolean = false
        private set

    override fun frame() {
        super.frame()

        if (
            isPaused ||
            isFinished
        ) return
        completedFrames++
    }

    public fun pause() {
        isPaused = true
    }

    public fun resume() {
        isPaused = false
    }

}
