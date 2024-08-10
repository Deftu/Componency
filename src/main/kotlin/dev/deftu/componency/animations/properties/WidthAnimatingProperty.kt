package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.WidthProperty

public class WidthAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: WidthProperty,
    private val newValue: WidthProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), WidthProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateWidth(component: Component): Float {
        val startWidth = oldValue.getWidth(component)
        val endWidth = newValue.getWidth(component)
        return startWidth + (endWidth - startWidth) * progress
    }

}
