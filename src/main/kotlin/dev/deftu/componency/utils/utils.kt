package dev.deftu.componency.utils

import dev.deftu.componency.platform.Platform
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sign

public fun roundToPixel(pixelRatio: Float, value: Float): Float {
    return round(value * pixelRatio).let { pixel ->
        if (pixel == 0f && abs(value) > 0.001f) {
            sign(value)
        } else {
            pixel
        }
    } / pixelRatio
}

public fun roundToPixel(platform: Platform, value: Float): Float {
    return roundToPixel(platform.pixelRatio, value)
}
