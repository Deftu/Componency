package dev.deftu.componency.android.platform

import android.content.Context
import android.view.View
import dev.deftu.componency.android.platform.audio.AndroidAudioEngine
import dev.deftu.componency.android.platform.font.AndroidFontLoader
import dev.deftu.componency.android.platform.image.AndroidImageLoader
import dev.deftu.componency.android.platform.input.AndroidInputHandler
import dev.deftu.componency.android.platform.rendering.AndroidRenderer
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock

public class AndroidPlatform(
    private val view: View,
    private val context: Context
) : Platform {

    // You should provide this externally when starting a frame
    public var currentCanvas: android.graphics.Canvas? = null

    override val viewportWidth: Float
        get() = context.resources.displayMetrics.widthPixels.toFloat()

    override val viewportHeight: Float
        get() = context.resources.displayMetrics.heightPixels.toFloat()

    override val pixelRatio: Float
        get() = context.resources.displayMetrics.density

    override val clock: Clock = DefaultClock

    override val inputHandler: AndroidInputHandler = AndroidInputHandler(view, context)

    override val renderer: AndroidRenderer = AndroidRenderer(
        canvasProvider = { currentCanvas },
        tempDirProvider = { context.cacheDir }
    )

    override val fontLoader: AndroidFontLoader = AndroidFontLoader(context)

    override val imageLoader: AndroidImageLoader = AndroidImageLoader

    override val audioEngine: AndroidAudioEngine = AndroidAudioEngine

}
