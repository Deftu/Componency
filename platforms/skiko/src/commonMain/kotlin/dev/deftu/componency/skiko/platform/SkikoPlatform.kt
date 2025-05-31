package dev.deftu.componency.skiko.platform

import dev.deftu.componency.platform.Platform
import dev.deftu.componency.platform.audio.AudioEngine
import dev.deftu.componency.platform.font.FontLoader
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.input.InputHandler
import dev.deftu.componency.skiko.platform.rendering.SkikoRenderer
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock
import kotlin.jvm.JvmOverloads

public class SkikoPlatform @JvmOverloads constructor(
    override val clock: Clock = DefaultClock
) : Platform {

    override var viewportWidth: Float = 0f

    override var viewportHeight: Float = 0f

    override var pixelRatio: Float = 0f

    override val inputHandler: InputHandler
        get() = TODO("Not yet implemented")

    override val renderer: SkikoRenderer = SkikoRenderer(
        initialWidth = viewportWidth.toInt(),
        initialHeight = viewportHeight.toInt()
    )

    override val fontLoader: FontLoader
        get() = TODO("Not yet implemented")

    override val imageLoader: ImageLoader
        get() = TODO("Not yet implemented")

    override val audioEngine: AudioEngine
        get() = TODO("Not yet implemented")

}
