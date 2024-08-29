package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.ColorProperty
import kotlin.math.roundToInt

public class ColorAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: ColorProperty,
    public val newValue: ColorProperty,
    delayFrames: Int
) : AnimatingProperty<Color>(easing, totalFrames, delayFrames), ColorProperty {

    override var cachedValue: Color = Color.WHITE
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateColor(component: Component): Color {
        val startColor = oldValue.getColor(component)
        val endColor = newValue.getColor(component)

        val red = startColor.red + (endColor.red - startColor.red) * progress
        val green = startColor.green + (endColor.green - startColor.green) * progress
        val blue = startColor.blue + (endColor.blue - startColor.blue) * progress
        val alpha = startColor.alpha + (endColor.alpha - startColor.alpha) * progress

        return Color.rgba(red.roundToInt(), green.roundToInt(), blue.roundToInt(), alpha.roundToInt())
    }

}
