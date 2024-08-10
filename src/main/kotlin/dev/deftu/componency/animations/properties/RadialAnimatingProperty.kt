package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.RadialProperty

public class RadialAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: RadialProperty,
    private val newValue: RadialProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), RadialProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateRadius(component: Component): Float {
        val startRadius = oldValue.getRadius(component)
        val endRadius = newValue.getRadius(component)
        return startRadius + (endRadius - startRadius) * progress
    }

}
