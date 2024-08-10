package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.HeightProperty

public class HeightAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: HeightProperty,
    private val newValue: HeightProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), HeightProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateHeight(component: Component): Float {
        val startHeight = oldValue.getHeight(component)
        val endHeight = newValue.getHeight(component)
        return startHeight + (endHeight - startHeight) * progress
    }

}