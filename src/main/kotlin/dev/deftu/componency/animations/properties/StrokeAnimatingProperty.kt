package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.dsl.minus
import dev.deftu.componency.dsl.plus
import dev.deftu.componency.dsl.times
import dev.deftu.componency.properties.StrokeProperty
import dev.deftu.componency.styling.Stroke

public class StrokeAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: StrokeProperty,
    public val newValue: StrokeProperty,
    delayFrames: Int
) : AnimatingProperty<Stroke>(easing, totalFrames, delayFrames), StrokeProperty {

    override var cachedValue: Stroke = Stroke.NONE
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateStroke(component: Component): Stroke {

        val startStroke = oldValue.getStroke(component)
        val endStroke = newValue.getStroke(component)

        val startColor = startStroke.color
        val endColor = endStroke.color

        val color = if (
            startColor.red != endColor.red ||
            startColor.green != endColor.green ||
            startColor.blue != endColor.blue ||
            startColor.alpha != endColor.alpha
        ) {
            // Animate from one color to the other
            val red = startColor.red + (endColor.red - startColor.red) * progress
            val green = startColor.green + (endColor.green - startColor.green) * progress
            val blue = startColor.blue + (endColor.blue - startColor.blue) * progress
            val alpha = startColor.alpha + (endColor.alpha - startColor.alpha) * progress

            Color.rgba(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())
        } else {
            // If they're the same, just use the start color
            startStroke.color
        }

        val width = startStroke.width + (endStroke.width - startStroke.width) * progress

        return Stroke(
            color,
            // Animate from one width to the other
            width,
            // No matter what, the sides should be the same. Even if the other stroke has a different set of sides defined.
            startStroke.sides
        )
    }

}
