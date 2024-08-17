package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.AngleProperty

public class AngleAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: AngleProperty,
    public val newValue: AngleProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), AngleProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateAngle(component: Component): Float {
        val startAngle = oldValue.getAngle(component)
        val endAngle = newValue.getAngle(component)
        return startAngle + (endAngle - startAngle) * progress
    }

}
