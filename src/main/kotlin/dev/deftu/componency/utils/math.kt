package dev.deftu.componency.utils

import dev.deftu.componency.color.Color
import dev.deftu.componency.stroke.Stroke
import kotlin.math.roundToInt

public fun interpolateInt(start: Int, end: Int, progress: Float): Int {
    return (start + (end - start) * progress).roundToInt()
}

public fun interpolateFloat(start: Float, end: Float, progress: Float): Float {
    return start + (end - start) * progress
}

public fun interpolateColor(start: Color, end: Color, progress: Float): Color {
    val red = interpolateInt(start.red, end.red, progress)
    val green = interpolateInt(start.green, end.green, progress)
    val blue = interpolateInt(start.blue, end.blue, progress)
    val alpha = interpolateInt(start.alpha, end.alpha, progress)
    return Color.rgba(red, green, blue, alpha)
}

public fun interpolateStroke(start: Stroke, end: Stroke, progress: Float): Stroke {
    val startColor = start.color
    val endColor = end.color
    val newColor = if (startColor != endColor) {
        interpolateColor(startColor, endColor, progress)
    } else {
        startColor
    }

    val newWidth = interpolateFloat(start.width, end.width, progress)
    return Stroke(newColor, newWidth, start.sides)
}
