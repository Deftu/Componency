package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.XProperty

public class XAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: XProperty,
    private val newValue: XProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), XProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateX(component: Component): Float {
        val startX = oldValue.getX(component)
        val endX = newValue.getX(component)
        return startX + (endX - startX) * progress
    }

}
