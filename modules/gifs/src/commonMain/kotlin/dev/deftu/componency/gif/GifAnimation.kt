package dev.deftu.componency.gif

import dev.deftu.componency.ByteStream
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlin.math.roundToInt

public data class GifAnimation(
    public val frames: List<GifFrame>,
    public var loopCount: Int = 0,
) {

    public companion object {

        @JvmField
        public val EMPTY: GifAnimation = GifAnimation(emptyList())

        @JvmStatic
        public fun decode(stream: ByteStream): GifAnimation {
            val decoder = GifDecoder(stream)
            return decoder.decode()
        }

    }

    private var currentFrameIndex: Int = 0
    private var accumulatedTime: Int = 0
    private var completedLoops: Int = 0

    public var isFinished: Boolean = false
        private set

    public var isPaused: Boolean = false
        private set

    public val isPlaying: Boolean
        get() = !isPaused && !isFinished

    public var playbackSpeed: Float = 1f
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    public val currentFrame: GifFrame
        get() = frames.getOrElse(currentFrameIndex) { frames.lastOrNull() } ?: throw IllegalStateException("No frames available")

    public fun step(deltaTime: Float) {
        if (isFinished || isPaused || frames.isEmpty()) {
            return
        }

        accumulatedTime += (deltaTime * playbackSpeed * 1_000f).roundToInt()
        while (frames.isNotEmpty() && accumulatedTime >= frames[currentFrameIndex].delay) {
            accumulatedTime -= frames[currentFrameIndex].delay
            currentFrameIndex++

            if (currentFrameIndex >= frames.size) {
                completedLoops++
                if (loopCount != 0 && completedLoops >= loopCount) {
                    // If a finite loopCount is specified, and we've completed it, freeze at last frame
                    currentFrameIndex = frames.size - 1
                    isFinished = true
                    break
                } else {
                    currentFrameIndex = 0
                }
            }
        }
    }

    public fun pause() {
        isPaused = true
    }

    public fun resume() {
        isPaused = false
    }

    public fun play() {
        reset()
        isPaused = false
    }

    public fun reset() {
        currentFrameIndex = 0
        accumulatedTime = 0
        completedLoops = 0
        isFinished = false
    }

}
