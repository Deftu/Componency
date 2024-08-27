package dev.deftu.componency.animations.properties

import dev.deftu.componency.animations.AnimatingProperty
import dev.deftu.componency.animations.Easing
import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.YProperty

public class YAnimatingProperty(
    easing: Easing,
    totalFrames: Int,
    private val oldValue: YProperty,
    public val newValue: YProperty,
    delayFrames: Int
) : AnimatingProperty<Float>(easing, totalFrames, delayFrames), YProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super<AnimatingProperty>.frame()
        oldValue.frame()
        newValue.frame()
    }

    override fun calculateY(component: Component): Float {
        val startY = oldValue.getY(component)
        val endY = newValue.getY(component)
        return startY + (endY - startY) * progress
    }

}
